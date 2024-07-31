package com.lckback.lckforall.match.service;

import com.lckback.lckforall.match.dto.MatchInfoDto;
import com.lckback.lckforall.match.model.Match;
import com.lckback.lckforall.match.repository.MatchRepository;
import com.lckback.lckforall.vote.model.MatchVote;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {
	private final MatchRepository matchRepository;

	public List<MatchInfoDto.TodayMatchResponse> todayMatchInfo() { // today match의 정보를 List 형식으로 리턴
		List<Match> todayMatches = matchRepository.findMatchesByDate(LocalDateTime.now());
		return todayMatches.stream()
			.map(match -> new MatchInfoDto.TodayMatchResponse(match.getId(), match.getMatchDate(),
				match.getTeam1().getTeamName(), match.getTeam1().getTeamLogoUrl(),
				match.getTeam2().getTeamName(), match.getTeam2().getTeamLogoUrl(),
				calculateMatchVoteResult(match.getMatchVotes(), match.getTeam1().getId()), // team1
				calculateMatchVoteResult(match.getMatchVotes(), match.getTeam2().getId()),
				match.getSeason().getName(),
				match.getMatchNumber())) // team2
			.collect(Collectors.toList());
	}

	public Double calculateMatchVoteResult(List<MatchVote> votes, Long teamId) { // 해당 팀의 승부 예측결과 %단위로 리턴

		int teamVotes = 0;
		int totalVotes = votes.size();

		// 팀의 투표 수 집계
		for (MatchVote vote : votes) {
			if (vote.getTeam().getId().equals(teamId)) {
				teamVotes++;
			}
		}

		// 득표율 계산
		return (totalVotes > 0) ? (teamVotes / (double)totalVotes) * 100 : 0.0;
	}

}
