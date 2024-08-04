package com.lckback.lckforall.match.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lckback.lckforall.base.type.MatchResult;
import com.lckback.lckforall.match.dto.MatchInfoDto;
import com.lckback.lckforall.match.model.Match;
import com.lckback.lckforall.match.repository.MatchRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HomeService {
	private final MatchRepository matchRepository;

	public MatchInfoDto.HomePageResponse getHomePageInformation() { // today match의 정보를 List 형식으로 리턴
		//today's match 정보
		LocalDate todayDate = LocalDate.now();
		LocalDateTime start = LocalDateTime.of(todayDate.minusDays(1), LocalTime.of(0, 0, 0));
		LocalDateTime end = todayDate.atTime(23, 59, 59);
		List<Match> todayMatches = matchRepository.findMatchesByMatchDateBetween(start, end);
		List<MatchInfoDto.TodayMatchDto> todayMatchList = todayMatches.stream()
			.map(match -> new MatchInfoDto.TodayMatchDto(match.getId(), match.getMatchDate(),
				match.getTeam1().getTeamName(), match.getTeam1().getTeamLogoUrl(),
				match.getTeam2().getTeamName(), match.getTeam2().getTeamLogoUrl(),
				match.getSeason().getName(),
				match.getMatchNumber())) // team2
			.collect(Collectors.toList());

		//최근 종료된 2경기의 경기결과
		List<Match> recentResults = matchRepository.findRecentMatches(MatchResult.NOT_FINISHED, PageRequest.of(0, 2));
		List<MatchInfoDto.RecentMatchResultDto> recentMatchResultDtos = recentResults.stream()
			.map(match -> new MatchInfoDto.RecentMatchResultDto(match.getId(), match.getMatchDate(),
				match.getTeam1().getTeamName(), match.getTeam1().getTeamLogoUrl(),
				match.getTeam2().getTeamName(), match.getTeam2().getTeamLogoUrl(),
				match.getMatchResult()))
			.collect(Collectors.toList());

		return new MatchInfoDto.HomePageResponse(todayMatchList, recentMatchResultDtos);
	}
}
