package com.lckback.lckforall.match;

import com.lckback.lckforall.base.type.UserRole;
import com.lckback.lckforall.base.type.UserStatus;
import com.lckback.lckforall.match.dto.MatchInfoDto;
import com.lckback.lckforall.match.model.Match;
import com.lckback.lckforall.match.repository.MatchRepository;
import com.lckback.lckforall.match.service.MatchService;
import com.lckback.lckforall.team.model.Team;
import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.vote.model.MatchVote;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

public class MatchServiceTest {
	@Mock
	private MatchRepository matchRepository; // MatchRepository를 Mocking

	@InjectMocks
	private MatchService matchService; // MatchService에 Mock을 주입

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this); // Mock 객체 초기화
	}

	@Test
	public void testGetTodayMatches() {
		// Given
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

		User user1 = User.builder()
			.id(1L)
			.nickname("user1")
			.profileImageUrl("url")
			.role(UserRole.ROLE_USER)
			.status(UserStatus.ACTIVE)
			.team(team1)
			.build();

		User user2 = User.builder()
			.id(2L)
			.nickname("user2")
			.profileImageUrl("url")
			.role(UserRole.ROLE_USER)
			.status(UserStatus.ACTIVE)
			.team(team1)
			.build();

		Match match1 = Match.builder()
			.id(1L)
			.matchDate(LocalDateTime.now())
			.team1(team1)
			.team2(team2)
			.build();

		Match match2 = Match.builder()
			.id(2L)
			.matchDate(LocalDateTime.now())
			.team1(team2)
			.team2(team1)
			.build();

		List<Match> matchList = new ArrayList<>();
		matchList.add(match1);
		matchList.add(match2);
		MatchVote matchVote1 = new MatchVote(1L, match1, user1, team2);
		MatchVote matchVote2 = new MatchVote(2L, match1, user2, team2);
		MatchVote matchVote3 = new MatchVote(3L, match2, user1, team1);
		MatchVote matchVote4 = new MatchVote(4L, match2, user2, team2);
		List<MatchVote> matchVotes1 = new ArrayList<>();
		List<MatchVote> matchVotes2 = new ArrayList<>();
		//        matchVotes1.add(matchVote1);
		//        matchVotes1.add(matchVote2);
		//        matchVotes2.add(matchVote3);
		//        matchVotes2.add(matchVote4);
		match1.testSetMatchVotes(matchVotes1);
		match2.testSetMatchVotes(matchVotes2);

		// when(matchRepository.findMatchesByDate(LocalDateTime.now())).thenReturn(matchList);// Mock의 동작 정의

		// When
		List<MatchInfoDto.TodayMatchResponse> matchInfo = matchService.todayMatchInfo();

		// Then
		for (MatchInfoDto.TodayMatchResponse response : matchInfo) {
			System.out.println(response.getMatchDate());
			System.out.println(response.getTeam2VoteRate());
		}
	}
}
