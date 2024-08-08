package com.lckback.lckforall.vote.service;

import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lckback.lckforall.base.api.error.MatchErrorCode;
import com.lckback.lckforall.base.api.error.PlayerErrorCode;
import com.lckback.lckforall.base.api.error.SetErrorCode;
import com.lckback.lckforall.base.api.error.TeamErrorCode;
import com.lckback.lckforall.base.api.error.UserErrorCode;
import com.lckback.lckforall.base.api.error.VoteErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;
import com.lckback.lckforall.base.type.MatchResult;
import com.lckback.lckforall.base.type.PlayerRole;
import com.lckback.lckforall.match.model.Match;
import com.lckback.lckforall.match.model.Set;
import com.lckback.lckforall.match.repository.MatchRepository;
import com.lckback.lckforall.match.repository.SetRepository;
import com.lckback.lckforall.player.model.Player;
import com.lckback.lckforall.player.repository.PlayerRepository;
import com.lckback.lckforall.team.model.SeasonTeam;
import com.lckback.lckforall.team.model.Team;
import com.lckback.lckforall.team.repository.SeasonTeamRepository;
import com.lckback.lckforall.team.repository.TeamRepository;
import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.user.respository.UserRepository;
import com.lckback.lckforall.vote.dto.MatchVoteDto;
import com.lckback.lckforall.vote.dto.SetVoteDto;
import com.lckback.lckforall.vote.model.MatchPogVote;
import com.lckback.lckforall.vote.model.MatchVote;
import com.lckback.lckforall.vote.model.SetPogVote;
import com.lckback.lckforall.vote.repository.MatchPogVoteRepository;
import com.lckback.lckforall.vote.repository.MatchVoteRepository;
import com.lckback.lckforall.vote.repository.SetPogVoteRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class VoteService {
	private final MatchVoteRepository matchVoteRepository;
	private final MatchRepository matchRepository;
	private final UserRepository userRepository;
	private final TeamRepository teamRepository;
	private final MatchPogVoteRepository matchPogVoteRepository;
	private final SeasonTeamRepository seasonTeamRepository;
	private final PlayerRepository playerRepository;
	private final SetRepository setRepository;
	private final SetPogVoteRepository setPogVoteRepository;

	public MatchVoteDto.MatchPredictionCandidateResponse getMatchPredictionCandidate(
		MatchVoteDto.MatchPredictionCandidateDto dto) {
		String kakaoUserId = dto.getKakaoUserId();
		User user = userRepository.findByKakaoUserId(kakaoUserId)
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));
		Long userId = user.getId();
		Long matchId = dto.getMatchId();

		if (matchVoteRepository.existsByMatchIdAndUserId(matchId, userId)) {
			throw new RestApiException(VoteErrorCode.ALREADY_VOTE);
		}
		Match match = matchRepository.findById(matchId)
			.orElseThrow(() -> new RestApiException(MatchErrorCode.NOT_EXIST_MATCH));
		return new MatchVoteDto.MatchPredictionCandidateResponse(match.getSeason().getName(), match.getMatchNumber()
			, match.getTeam1().getId(), match.getTeam1().getTeamLogoUrl()
			, match.getTeam2().getId(), match.getTeam2().getTeamLogoUrl());
	}

	public void doMatchPredictionVote(MatchVoteDto.MatchPredictionDto dto) {
		String kakaoUserId = dto.getKakaoUserId();
		User user = userRepository.findByKakaoUserId(kakaoUserId)
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));
		Long userId = user.getId();
		Long matchId = dto.getMatchId();
		Match match = matchRepository.findById(dto.getMatchId())
			.orElseThrow(() -> new RestApiException(MatchErrorCode.NOT_EXIST_MATCH));

		if(!match.getVotable()){
			throw new RestApiException(VoteErrorCode.NOT_VOTE_TIME);
		}

		Team team = teamRepository.findById(dto.getTeamId())
			.orElseThrow(() -> new RestApiException(TeamErrorCode.NOT_EXISTS_TEAM));

		if (matchVoteRepository.existsByMatchIdAndUserId(matchId, userId)) { // 예외처리
			throw new RestApiException(VoteErrorCode.ALREADY_VOTE);
		}
		// 투표 생성
		MatchVote vote = MatchVote.builder()
			.user(user)
			.match(match)
			.team(team)
			.build();
		matchVoteRepository.save(vote);
	}

	public MatchVoteDto.MatchPogVoteCandidateResponse getMatchPogCandidate(
		MatchVoteDto.MatchPredictionCandidateDto dto) {
		String kakaoUserId = dto.getKakaoUserId();
		User user = userRepository.findByKakaoUserId(kakaoUserId)
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));
		Long userId = user.getId();
		Long matchId = dto.getMatchId();

		// 투표를 이미 한 경우 (투표한 선수의 정보 리턴)
		if (matchPogVoteRepository.votedPlayerByMatchIdAndUserId(matchId, userId).isPresent()) {
			Player votedPlayer = matchPogVoteRepository.votedPlayerByMatchIdAndUserId(matchId, userId)
				.orElseThrow(() -> new RestApiException(VoteErrorCode.NOT_EXIST_VOTE));
			MatchVoteDto.MatchPogVoteCandidateResponse response = new MatchVoteDto.MatchPogVoteCandidateResponse(
				Collections.singletonList(new MatchVoteDto.PlayerInformation(
					votedPlayer.getId(),
					votedPlayer.getProfileImageUrl(),
					votedPlayer.getName()
				))
			);
			return response;
		}

		Match match = matchRepository.findById(matchId)
			.orElseThrow(() -> new RestApiException(MatchErrorCode.NOT_EXIST_MATCH));
		// match pog vote 시간이 아닌 경우
		if(!match.getMatchPogVotable()){
			throw new RestApiException(VoteErrorCode.NOT_VOTE_TIME);
		}

		// 투표를 하지 않았고 투표 시간이 남은 경우
		if (match.getMatchResult().equals(MatchResult.TEAM1_WIN)) { // team1 win
			SeasonTeam seasonTeam1 = seasonTeamRepository.findBySeasonAndTeam(match.getSeason(), match.getTeam1())
				.orElseThrow(() -> new RestApiException(TeamErrorCode.NOT_EXISTS_TEAM));

			return new MatchVoteDto.MatchPogVoteCandidateResponse(
				seasonTeam1.getSeasonTeamPlayers().stream()
					.filter(seasonTeamPlayer -> seasonTeamPlayer.getPlayer().getRole() == PlayerRole.LCK_ROSTER)
					.map(seasonTeamPlayer -> new MatchVoteDto.PlayerInformation(
						seasonTeamPlayer.getPlayer().getId(),
						seasonTeamPlayer.getPlayer().getProfileImageUrl(),
						seasonTeamPlayer.getPlayer().getName()))
					.collect(Collectors.toList()));
		}
		// team2 win
		SeasonTeam seasonTeam2 = seasonTeamRepository.findBySeasonAndTeam(match.getSeason(), match.getTeam2())
			.orElseThrow(() -> new RestApiException(TeamErrorCode.NOT_EXISTS_TEAM));

		return new MatchVoteDto.MatchPogVoteCandidateResponse(
			seasonTeam2.getSeasonTeamPlayers().stream()
				.filter(seasonTeamPlayer -> seasonTeamPlayer.getPlayer().getRole() == PlayerRole.LCK_ROSTER)
				.map(seasonTeamPlayer -> new MatchVoteDto.PlayerInformation(
					seasonTeamPlayer.getPlayer().getId(),
					seasonTeamPlayer.getPlayer().getProfileImageUrl(),
					seasonTeamPlayer.getPlayer().getName()))
				.collect(Collectors.toList()));

	}

	public void doMatchPogVote(MatchVoteDto.MatchPogVoteDto dto) {
		String kakaoUserId = dto.getKakaoUserId();
		User user = userRepository.findByKakaoUserId(kakaoUserId)
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));
		Player player = playerRepository.findById(dto.getPlayerId())
			.orElseThrow(() -> new RestApiException(PlayerErrorCode.NOT_EXIST_PLAYER));
		Match match = matchRepository.findById(dto.getMatchId())
			.orElseThrow(() -> new RestApiException(MatchErrorCode.NOT_EXIST_MATCH));

		// match pog vote 시간이 아닌경우
		if(!match.getMatchPogVotable()){
			throw new RestApiException(VoteErrorCode.NOT_VOTE_TIME);
		}

		// 승리 팀의 플레이어에게만 투표 가능 (team1 승리 경우)
		if(match.getMatchResult().equals(MatchResult.TEAM1_WIN)){
			SeasonTeam seasonTeam = seasonTeamRepository.findBySeasonAndTeam(match.getSeason(), match.getTeam1())
				.orElseThrow(() -> new RestApiException(TeamErrorCode.NOT_EXISTS_TEAM));
			if(!seasonTeam.getSeasonTeamPlayers().stream()
				.anyMatch(seasonTeamPlayer -> seasonTeamPlayer.getPlayer().equals(player))){
				throw new RestApiException(VoteErrorCode.ONLY_VOTE_FOR_WINNER);
			}
		}
		// // 승리 팀의 플레이어에게만 투표 가능 (team2 승리 경우)
		if(match.getMatchResult().equals(MatchResult.TEAM2_WIN)){
			SeasonTeam seasonTeam = seasonTeamRepository.findBySeasonAndTeam(match.getSeason(), match.getTeam2())
				.orElseThrow(() -> new RestApiException(TeamErrorCode.NOT_EXISTS_TEAM));
			if(!seasonTeam.getSeasonTeamPlayers().stream()
				.anyMatch(seasonTeamPlayer -> seasonTeamPlayer.getPlayer().equals(player))){
				throw new RestApiException(VoteErrorCode.ONLY_VOTE_FOR_WINNER);
			}
		}


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

	// pog-player가 set내에 존재할때 로직 처리

	public SetVoteDto.SetPogVoteCandidateResponse getSetPogCandidate(SetVoteDto.VoteCandidateDto dto) {
		String kakaoUserId = dto.getKakaoUserId();
		User user = userRepository.findByKakaoUserId(kakaoUserId)
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));
		Long userId = user.getId();
		Integer setIndex = dto.getSetIndex();
		Long matchId = dto.getMatchId();
		// setIndex가 잘못 주어졌을 때 예외처리
		Set set = setRepository.findBySetIndexAndMatchId(setIndex, matchId)
			.orElseThrow(() -> new RestApiException(SetErrorCode.NOT_EXIST_SET));
		Match match = set.getMatch();
		// 투표를 이미 한 경우 (투표한 선수의 정보 리턴)
		if (setPogVoteRepository.votedPlayerBySetIdAndUserId(set.getId(), userId).isPresent()) {
			Player votedPlayer = setPogVoteRepository.votedPlayerBySetIdAndUserId(set.getId(), userId)
				.orElseThrow(() -> new RestApiException(VoteErrorCode.NOT_EXIST_VOTE));

			SetVoteDto.SetPogVoteCandidateResponse response = new SetVoteDto.SetPogVoteCandidateResponse(
				Collections.singletonList(new SetVoteDto.PlayerInformation(
					votedPlayer.getId(),
					votedPlayer.getProfileImageUrl(),
					votedPlayer.getName()
				))
			);
			return response;
		}
		// 투표를 시간이 아닌경우
		if (!set.getVotable()) {
			throw new RestApiException(VoteErrorCode.NOT_VOTE_TIME);
		}
		// 투표를 하지 않았고 투표 시간이 남은 경우
		if (set.getWinnerTeam().equals(match.getTeam1())) { // team1 win
			SeasonTeam seasonTeam1 = seasonTeamRepository.findBySeasonAndTeam(match.getSeason(), match.getTeam1())
				.orElseThrow(() -> new RestApiException(TeamErrorCode.NOT_EXISTS_TEAM));

			return new SetVoteDto.SetPogVoteCandidateResponse(
				seasonTeam1.getSeasonTeamPlayers().stream()
					.filter(seasonTeamPlayer -> seasonTeamPlayer.getPlayer().getRole() == PlayerRole.LCK_ROSTER)
					.map(seasonTeamPlayer -> new SetVoteDto.PlayerInformation(
						seasonTeamPlayer.getPlayer().getId(),
						seasonTeamPlayer.getPlayer().getProfileImageUrl(),
						seasonTeamPlayer.getPlayer().getName()))
					.collect(Collectors.toList()));
		} // team2 win
		SeasonTeam seasonTeam2 = seasonTeamRepository.findBySeasonAndTeam(match.getSeason(), match.getTeam2())
			.orElseThrow(() -> new RestApiException(TeamErrorCode.NOT_EXISTS_TEAM));

		return new SetVoteDto.SetPogVoteCandidateResponse(
			seasonTeam2.getSeasonTeamPlayers().stream()
				.filter(seasonTeamPlayer -> seasonTeamPlayer.getPlayer().getRole() == PlayerRole.LCK_ROSTER)
				.map(seasonTeamPlayer -> new SetVoteDto.PlayerInformation(
					seasonTeamPlayer.getPlayer().getId(),
					seasonTeamPlayer.getPlayer().getProfileImageUrl(),
					seasonTeamPlayer.getPlayer().getName()))
				.collect(Collectors.toList()));

	}

	public void doSetPogVote(SetVoteDto.SetPogVoteDto dto) {
		String kakaoUserId = dto.getKakaoUserId();
		User user = userRepository.findByKakaoUserId(kakaoUserId)
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));
		Player player = playerRepository.findById(dto.getPlayerId())
			.orElseThrow(() -> new RestApiException(PlayerErrorCode.NOT_EXIST_PLAYER));
		Set set = setRepository.findBySetIndexAndMatchId(dto.getSetIndex(), dto.getMatchId())
			.orElseThrow(() -> new RestApiException(SetErrorCode.NOT_EXIST_SET));
		Match match = matchRepository.findById(dto.getMatchId())
			.orElseThrow(() -> new RestApiException(MatchErrorCode.NOT_EXIST_MATCH));

		if(!set.getVotable()){ // 투표 시간이 아닌 경우
			throw new RestApiException(VoteErrorCode.NOT_VOTE_TIME);
		}

		// 승리 팀의 플레이어에게만 투표 가능 (team1 승리 경우)
		if(set.getWinnerTeam() == match.getTeam1()){
			SeasonTeam seasonTeam = seasonTeamRepository.findBySeasonAndTeam(match.getSeason(), match.getTeam1())
				.orElseThrow(() -> new RestApiException(TeamErrorCode.NOT_EXISTS_TEAM));
			if(!seasonTeam.getSeasonTeamPlayers().stream()
				.anyMatch(seasonTeamPlayer -> seasonTeamPlayer.getPlayer().equals(player))){
				throw new RestApiException(VoteErrorCode.ONLY_VOTE_FOR_WINNER);
			}
		}

		// 승리 팀의 플레이어에게만 투표 가능 (team2 승리 경우)
		if(set.getWinnerTeam() == match.getTeam2()){
			SeasonTeam seasonTeam = seasonTeamRepository.findBySeasonAndTeam(match.getSeason(), match.getTeam2())
				.orElseThrow(() -> new RestApiException(TeamErrorCode.NOT_EXISTS_TEAM));
			if(!seasonTeam.getSeasonTeamPlayers().stream()
				.anyMatch(seasonTeamPlayer -> seasonTeamPlayer.getPlayer().equals(player))){
				throw new RestApiException(VoteErrorCode.ONLY_VOTE_FOR_WINNER);
			}
		}

		// 이미 투표 했다면 예외처리
		if (setPogVoteRepository.existsBySetIdAndUserId(set.getId(), user.getId())) {
			throw new RestApiException(VoteErrorCode.ALREADY_VOTE);
		}
		SetPogVote vote = SetPogVote.builder()
			.set(set)
			.user(user)
			.player(player)
			.build();
		setPogVoteRepository.save(vote);
	}
}
