package com.lckback.lckforall.match;

import com.lckback.lckforall.base.type.PlayerRole;
import com.lckback.lckforall.base.type.UserRole;
import com.lckback.lckforall.base.type.UserStatus;
import com.lckback.lckforall.match.dto.PogInfoDto;
import com.lckback.lckforall.match.model.Match;
import com.lckback.lckforall.match.model.Set;
import com.lckback.lckforall.match.repository.MatchRepository;
import com.lckback.lckforall.match.service.PogService;
import com.lckback.lckforall.player.model.Player;
import com.lckback.lckforall.player.repository.PlayerRepository;
import com.lckback.lckforall.team.model.Season;
import com.lckback.lckforall.team.model.Team;
import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.vote.model.MatchPogVote;
import com.lckback.lckforall.vote.model.SetPogVote;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PogServiceTest {
	@Mock
	private PlayerRepository playerRepository; // PlaerRepository를 Mocking
	@Mock
	private MatchRepository matchRepository;

	@InjectMocks
	private PogService pogService; // PogService에 Mock을 주입

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
		User user3 = User.builder()
			.id(3L)
			.nickname("user3")
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

		Set set = Set.builder()
			.id(1L)
			.setIndex(1)
			.match(match1)
			.build();

		Player player1 = Player.builder()
			.id(1L)
			.name("player1")
			.ProfileImageUrl("imgUrl")
			.role(PlayerRole.LCK_ROSTER)
			.build();
		Player player2 = Player.builder()
			.id(2L)
			.name("player2")
			.ProfileImageUrl("imgUrl")
			.role(PlayerRole.LCK_ROSTER)
			.build();
		Season season = Season.builder()
			.id(1L)
			.name("test season")
			.build();
		//set vote 테스트
		SetPogVote setVote1 = SetPogVote.builder()
			.id(1L).set(set).player(player1).user(user1).build();
		SetPogVote setVote2 = SetPogVote.builder()
			.id(2L).set(set).player(player2).user(user2).build();
		SetPogVote setVote3 = SetPogVote.builder()
			.id(3L).set(set).player(player1).user(user3).build();
		List<SetPogVote> setVoteList = new ArrayList<>();
		setVoteList.add(setVote1);
		setVoteList.add(setVote2);
		setVoteList.add(setVote3);
		set.setVoteList(setVoteList);
		List<Set> sets = new ArrayList<>();
		sets.add(set);

		MatchPogVote vote1 = MatchPogVote.builder()
			.id(1L).match(match1).player(player2).user(user1).build();
		MatchPogVote vote2 = MatchPogVote.builder()
			.id(2L).match(match1).player(player1).user(user2).build();
		MatchPogVote vote3 = MatchPogVote.builder()
			.id(3L).match(match1).player(player2).user(user3).build();
		List<MatchPogVote> voteList = new ArrayList<>();
		voteList.add(vote1);
		voteList.add(vote2);
		voteList.add(vote3);
		match1.testSetPogVote(voteList);
		match1.setSets(sets);
		match1.setSeason(season);
		//player1가 match pog인 경우
		when(playerRepository.findById(1L)).thenReturn(Optional.ofNullable(player1));// Mock의 동작 정의
		when(matchRepository.findById(1L)).thenReturn(Optional.ofNullable(match1)); // 1번 match에 대해 정의

		// When
		PogInfoDto.PogServiceDto dto = new PogInfoDto.PogServiceDto(1L);
		// PogInfoDto.PogResponse matchPogResponse = pogService.matchPog(dto); // match pog
		PogInfoDto.PogResponse setPogResponse = pogService.findSetPog(dto, 1); // set pog

		// Then
		// assertEquals(2L, matchPogResponse.getId()); // match pog 결과 검증
		// System.out.println(matchPogResponse.getId() + " " + matchPogResponse.getName() + " " + matchPogResponse.getProfileImageUrl());

		assertEquals(1L, setPogResponse.getId()); // set pog 결과 검증
		System.out.println(
			setPogResponse.getId() + " " + setPogResponse.getName() + " " + setPogResponse.getProfileImageUrl());
	}
}
