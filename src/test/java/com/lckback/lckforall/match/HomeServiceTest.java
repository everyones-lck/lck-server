package com.lckback.lckforall.match;

import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;

import com.lckback.lckforall.base.type.MatchResult;
import com.lckback.lckforall.match.dto.MatchInfoDto;
import com.lckback.lckforall.match.model.Match;
import com.lckback.lckforall.match.repository.MatchRepository;
import com.lckback.lckforall.match.service.HomeService;
import com.lckback.lckforall.team.model.Season;
import com.lckback.lckforall.team.model.Team;

public class HomeServiceTest {
	@Mock
	private MatchRepository matchRepository; // MatchRepository를 Mocking

	@InjectMocks
	private HomeService homeService; // HomeService에 Mock을 주입

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this); // Mock 객체 초기화
	}

	@Test
	public void testHomePage() {
		Season season = Season.builder()
			.id(1L)
			.name("test season")
			.build();
		Team team1 = Team.builder()
			.id(1L)
			.teamName("team1")
			.teamLogoUrl("url")
			.build();

		Team team2 = Team.builder()
			.id(2L)
			.teamName("team2")
			.teamLogoUrl("url")
			.build();

		Match match1 = Match.builder()
			.id(1L)
			.matchDate(LocalDateTime.now())
			.team1(team1)
			.team2(team2)
			.season(season)
			.matchResult(MatchResult.TEAM1_WIN)
			.build();

		Match match2 = Match.builder()
			.id(2L)
			.matchDate(LocalDateTime.now())
			.team1(team1)
			.team2(team2)
			.season(season)
			.matchResult(MatchResult.TEAM2_WIN)
			.build();
		List<Match> matchList = new ArrayList<>();
		matchList.add(match1); matchList.add(match2);

		LocalDate todayDate = LocalDate.now();
		LocalDateTime start = LocalDateTime.of(todayDate.minusDays(1), LocalTime.of(0, 0, 0));
		LocalDateTime end = todayDate.atTime(23, 59, 59);
		when(matchRepository.findMatchesByMatchDateBetween(start, end)).thenReturn(matchList);// Mock의 동작 정의

		when(matchRepository.findRecentMatches(MatchResult.NOT_FINISHED, PageRequest.of(0, 2))).thenReturn(matchList);// Mock의 동작 정의
		// When
		MatchInfoDto.HomePageResponse response = homeService.getHomePageInformation();

		// Then
		// assertEquals(2L, matchPogResponse.getId()); // match pog 결과 검증
		// System.out.println(matchPogResponse.getId() + " " + matchPogResponse.getName() + " " + matchPogResponse.getProfileImageUrl());

		List<MatchInfoDto.TodayMatchDto> todayMatchDtos = response.getTodayMatches();
		List<MatchInfoDto.RecentMatchResultDto> recentMatchResultDtos = response.getRecentMatchResults();
		for(MatchInfoDto.RecentMatchResultDto result: recentMatchResultDtos){
			System.out.println(result.getMatchResult());
		}
		for(MatchInfoDto.TodayMatchDto result: todayMatchDtos){
			System.out.println(result.getMatchId());
		}
	}
}
