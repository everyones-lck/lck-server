package com.lckback.lckforall.team.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lckback.lckforall.player.model.SeasonTeamPlayer;
import com.lckback.lckforall.team.model.Season;
import com.lckback.lckforall.team.model.SeasonTeam;
import com.lckback.lckforall.team.model.Team;

public interface SeasonTeamRepository extends JpaRepository<SeasonTeam, Long> {
	@EntityGraph(attributePaths = {"team"})
	Page<SeasonTeam> findAllBySeasonOrderByRatingAsc(Season season, Pageable pageable);

	@EntityGraph(attributePaths = {"season"})
	Page<SeasonTeam> findAllByTeamAndRatingAndSeasonIsNot(Team team, Integer rating, Season season, Pageable pageable);

	@EntityGraph(attributePaths = {"season"})
	Page<SeasonTeam> findAllByTeam(Team team, Pageable pageable);

	@EntityGraph(attributePaths = {"season"})
	Page<SeasonTeam> findAllByTeamAndSeasonIsNot(Team team, Season season, Pageable pageable);

	@EntityGraph(attributePaths = {"team", "season"})
	@Query("select st from SeasonTeam st join st.seasonTeamPlayers stp where stp in :seasonTeamPlayers")
	Page<SeasonTeam> findAllBySeasonTeamPlayersIn(List<SeasonTeamPlayer> seasonTeamPlayers, Pageable pageable);

	@EntityGraph(attributePaths = {"season"})
	@Query("select st from SeasonTeam st join st.seasonTeamPlayers stp where st.rating = 1 and stp in :seasonTeamPlayers and st.season != :season")
	Page<SeasonTeam> findWinningSeasonTeamBySeasonTeamPlayers(List<SeasonTeamPlayer> seasonTeamPlayers, Season season,
		Pageable pageable);

	Optional<SeasonTeam> findBySeasonAndTeam(Season season, Team team);
}
