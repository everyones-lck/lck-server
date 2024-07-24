package com.lckback.lckforall.model;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.lckback.lckforall.base.type.PlayerRole;
import com.lckback.lckforall.match.model.Match;
import com.lckback.lckforall.player.model.Player;
import com.lckback.lckforall.team.model.Team;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@SpringBootTest
@Transactional
public class TeamAllMatchesTest {

	@PersistenceContext
	EntityManager em;

	private Long team1Id;

	@BeforeEach
	void initialize() {
		Team team1 = teamInitializer(1);
		Team team2 = teamInitializer(2);
		Player player = playerInitializer(team1);
		em.persist(team1);
		em.persist(team2);
		em.persist(player);
		for (int i = 0; i < 50; i++) {
			// matchInitializer(team1, team2, player); => team1 vs team2 & pogPlayer = player
			em.persist(matchInitializer(team1, team2, player));
			em.persist(matchInitializer(team2, team1, player));
		}
		team1Id = team1.getId();
		em.flush();
		em.clear();
	}

	@Test
	void allMatchesTest() {
		System.out.println("=================>load team1");
		Team team1 = em.find(Team.class, team1Id);
		System.out.println("=================>load team1 finish");
		Assertions.assertThat(team1.getAllMatches().size()).isEqualTo(100);
	}

	@Test
	void printAllMatchesFindByTeam() {
		em.find(Team.class, team1Id).getAllMatches().forEach(match ->
			System.out.println(match.getTeam1().getTeamName() + " vs " + match.getTeam2().getTeamName())
		);
	}

	private Team teamInitializer(Integer index) {
		return Team.builder()
			.teamName("teamName" + index)
			.teamLogoUrl("teamLogoUrl" + index)
			.season("testSeason" + index)
			.winningPoint(0)
			.build();
	}

	private Player playerInitializer(Team team) {
		return Player.builder()
			.name("playerName")
			.ProfileImageUrl("playerProfileImageUrl")
			.role(PlayerRole.LCK_ROSTER)
			.team(team)
			.build();
	}

	private Match matchInitializer(Team team1, Team team2, Player player) {
		return Match.builder()
			.matchDate(LocalDateTime.now())
			.team1(team1)
			.team2(team2)
			.pogPlayer(player)
			.build();
	}
}
