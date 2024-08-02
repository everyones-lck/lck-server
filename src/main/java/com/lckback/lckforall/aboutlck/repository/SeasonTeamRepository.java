package com.lckback.lckforall.aboutlck.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lckback.lckforall.team.model.Season;
import com.lckback.lckforall.team.model.SeasonTeam;
import com.lckback.lckforall.team.model.Team;

public interface SeasonTeamRepository extends JpaRepository<SeasonTeam, Long> {
	@EntityGraph(attributePaths = {"team"})
	Page<SeasonTeam> findAllBySeasonOrderByRatingAsc(Season season, Pageable pageable);

	@EntityGraph(attributePaths = {"season"})
	Page<SeasonTeam> findAllByTeamAndRating(Team team, Integer rating, Pageable pageable);

	@EntityGraph(attributePaths = {"season"})
	Page<SeasonTeam> findAllByTeam(Team team, Pageable pageable);
}
