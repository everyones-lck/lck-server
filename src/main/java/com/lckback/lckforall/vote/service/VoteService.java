package com.lckback.lckforall.vote.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lckback.lckforall.aboutlck.repository.TeamRepository;
import com.lckback.lckforall.base.api.error.MatchErrorCode;
import com.lckback.lckforall.base.api.error.TeamErrorCode;
import com.lckback.lckforall.base.api.error.UserErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;
import com.lckback.lckforall.match.model.Match;
import com.lckback.lckforall.match.repository.MatchRepository;
import com.lckback.lckforall.team.model.Team;
import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.user.respository.UserRepository;
import com.lckback.lckforall.vote.dto.MatchVoteDto;
import com.lckback.lckforall.vote.model.MatchVote;
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

	public MatchVoteDto.VoteCandidateResponse getCandidate(MatchVoteDto.VoteCandidateDto dto) {
		Long userId = dto.getUserId();
		Long matchId = dto.getMatchId();
		if (matchVoteRepository.existsByMatchIdAndUserId(matchId, userId)) {
			throw new RestApiException(MatchErrorCode.ALREADY_VOTE);
		}
		Match match = matchRepository.findById(matchId)
			.orElseThrow(() -> new RestApiException(MatchErrorCode.NOT_EXIST_MATCH));
		return new MatchVoteDto.VoteCandidateResponse(match.getSeason().getName(), match.getMatchNumber()
			, match.getTeam1().getId(), match.getTeam1().getTeamLogoUrl()
			, match.getTeam2().getId(), match.getTeam2().getTeamLogoUrl());
	}

	public void doVote(MatchVoteDto.MakeVoteDto dto) {
		Long userId = dto.getUserId();
		Long matchId = dto.getMatchId();
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));
		Match match = matchRepository.findById(dto.getMatchId())
			.orElseThrow(() -> new RestApiException(MatchErrorCode.NOT_EXIST_MATCH));
		Team team = teamRepository.findById(dto.getTeamId())
			.orElseThrow(() -> new RestApiException(TeamErrorCode.NOT_EXIST_TEAM));

		if (matchVoteRepository.existsByMatchIdAndUserId(matchId, userId)) { // 예외처리
			throw new RestApiException(MatchErrorCode.ALREADY_VOTE);
		}
		// 투표 생성
		MatchVote vote = MatchVote.builder()
			.user(user).match(match).team(team)
			.build();
		matchVoteRepository.save(vote);
	}
}
