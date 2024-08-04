package com.lckback.lckforall.vote.service;

import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lckback.lckforall.aboutlck.repository.TeamRepository;
import com.lckback.lckforall.base.api.error.MatchErrorCode;
import com.lckback.lckforall.base.api.error.PlayerErrorCode;
import com.lckback.lckforall.base.api.error.TeamErrorCode;
import com.lckback.lckforall.base.api.error.UserErrorCode;
import com.lckback.lckforall.base.api.error.VoteErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;
import com.lckback.lckforall.base.type.MatchResult;
import com.lckback.lckforall.base.type.PlayerRole;
import com.lckback.lckforall.match.model.Match;
import com.lckback.lckforall.match.repository.MatchRepository;
import com.lckback.lckforall.player.model.Player;
import com.lckback.lckforall.player.repository.PlayerRepository;
import com.lckback.lckforall.team.model.SeasonTeam;
import com.lckback.lckforall.team.model.Team;
import com.lckback.lckforall.team.repository.SeasonTeamRepository;
import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.user.respository.UserRepository;
import com.lckback.lckforall.vote.dto.MatchVoteDto;
import com.lckback.lckforall.vote.model.MatchPogVote;
import com.lckback.lckforall.vote.model.MatchVote;
import com.lckback.lckforall.vote.repository.MatchPogVoteRepository;
import com.lckback.lckforall.vote.repository.MatchVoteRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VoteService {
	private final MatchVoteRepository matchVoteRepository;
	private final MatchRepository matchRepository;
	private final UserRepository userRepository;
	private final TeamRepository teamRepository;
	private final MatchPogVoteRepository matchPogVoteRepository;
	private final SeasonTeamRepository seasonTeamRepository;
	private final PlayerRepository playerRepository;

	public MatchVoteDto.VoteCandidateResponse getCandidate(MatchVoteDto.VoteCandidateDto dto) {
		Long userId = dto.getUserId();
		Long matchId = dto.getMatchId();
		if (matchVoteRepository.existsByMatchIdAndUserId(matchId, userId)) {
			throw new RestApiException(VoteErrorCode.ALREADY_VOTE);
		}
		Match match = matchRepository.findById(matchId)
			.orElseThrow(() -> new RestApiException(MatchErrorCode.NOT_EXIST_MATCH));
		return new MatchVoteDto.VoteCandidateResponse(match.getSeason().getName(), match.getMatchNumber()
			, match.getTeam1().getId(), match.getTeam1().getTeamLogoUrl()
			, match.getTeam2().getId(), match.getTeam2().getTeamLogoUrl());
	}

	public void doMatchPredictionVote(MatchVoteDto.MatchPredictDto dto) {
		Long userId = dto.getUserId();
		Long matchId = dto.getMatchId();
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));
		Match match = matchRepository.findById(dto.getMatchId())
			.orElseThrow(() -> new RestApiException(MatchErrorCode.NOT_EXIST_MATCH));
		Team team = teamRepository.findById(dto.getTeamId())
			.orElseThrow(() -> new RestApiException(TeamErrorCode.NOT_EXIST_TEAM));

		if (matchVoteRepository.existsByMatchIdAndUserId(matchId, userId)) { // 예외처리
			throw new RestApiException(VoteErrorCode.ALREADY_VOTE);
		}
		// 투표 생성
		MatchVote vote = MatchVote.builder()
			.user(user).match(match).team(team)
			.build();
		matchVoteRepository.save(vote);
	}

	public MatchVoteDto.MatchPogVoteCandidateResponse getMatchPogCandidate(
		MatchVoteDto.VoteCandidateDto voteCandidateDto) {
		Long userId = voteCandidateDto.getUserId();
		Long matchId = voteCandidateDto.getMatchId();
		Match match = matchRepository.findById(matchId)
			.orElseThrow(() -> new RestApiException(MatchErrorCode.NOT_EXIST_MATCH));
		// 투표를 한 경우
		if (matchPogVoteRepository.existsByMatchIdAndUserId(matchId, userId)) {
			Player votedPlayer = matchPogVoteRepository.votedPlayerByMatchIdAndUserId(matchId, userId);
			MatchVoteDto.MatchPogVoteCandidateResponse response = new MatchVoteDto.MatchPogVoteCandidateResponse(
				Collections.singletonList(new MatchVoteDto.PlayerInformation(
					votedPlayer.getId(),
					votedPlayer.getProfileImageUrl(),
					votedPlayer.getName()
				))
			);
			return response;
		}
		// 투표를 하지 않았고 투표 시간이 다 된 경우
		if (!match.getVotable()) {
			throw new RestApiException(VoteErrorCode.VOTE_TIME_OVER);
		}
		// 아직 경기가 끝나지 않은 경우
		if (match.getMatchResult().equals(MatchResult.NOT_FINISHED)) {
			throw new RestApiException(VoteErrorCode.MATCH_NOT_END);
		}
		// 투표를 하지 않았고 투표 시간이 남은 경우
		SeasonTeam winnerTeam;
		if (match.getMatchResult().equals(MatchResult.TEAM1_WIN)) { // team1 win
			winnerTeam = seasonTeamRepository.findBySeasonAndTeam(match.getSeason(), match.getTeam1())
				.orElseThrow(() -> new RestApiException(TeamErrorCode.NOT_EXIST_TEAM));
		} else { // team2 win
			winnerTeam = seasonTeamRepository.findBySeasonAndTeam(match.getSeason(), match.getTeam2())
				.orElseThrow(() -> new RestApiException(TeamErrorCode.NOT_EXIST_TEAM));
		}
		return new MatchVoteDto.MatchPogVoteCandidateResponse(
			winnerTeam.getSeasonTeamPlayers().stream()
				.filter(seasonTeamPlayer -> seasonTeamPlayer.getPlayer().getRole() == PlayerRole.LCK_ROSTER)
				.map(seasonTeamPlayer -> new MatchVoteDto.PlayerInformation(
					seasonTeamPlayer.getPlayer().getId(),
					seasonTeamPlayer.getPlayer().getProfileImageUrl(),
					seasonTeamPlayer.getPlayer().getName()))
				.collect(Collectors.toList()));

	}

	public void doMatchPogVote(MatchVoteDto.MatchPogVoteDto dto) {
		Player player = playerRepository.findById(dto.getPlayerId())
			.orElseThrow(() -> new RestApiException(PlayerErrorCode.NOT_EXIST_PLAYER));
		Match match = matchRepository.findById(dto.getMatchId())
			.orElseThrow(() -> new RestApiException(MatchErrorCode.NOT_EXIST_MATCH));
		User user = userRepository.findById(dto.getUserId())
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));
		// 이미 투표 했다면 예외처리
		if (matchPogVoteRepository.existsByMatchIdAndUserId(match.getId(), user.getId())) {
			throw new RestApiException(VoteErrorCode.ALREADY_VOTE);
		}
		MatchPogVote vote = MatchPogVote.builder()
			.match(match)
			.user(user)
			.player(player)
			.build();
		matchPogVoteRepository.save(vote);
	}
}
