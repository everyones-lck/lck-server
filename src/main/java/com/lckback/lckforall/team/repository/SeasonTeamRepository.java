package com.lckback.lckforall.team.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lckback.lckforall.team.model.Season;
import com.lckback.lckforall.team.model.SeasonTeam;
import com.lckback.lckforall.team.model.Team;

@Repository("teamSeasonTeamRepository")
public interface SeasonTeamRepository extends JpaRepository<SeasonTeam, Long> {
	@Query("SELECT st FROM SeasonTeam st WHERE st.season = :season AND st.team = :team")
	Optional<SeasonTeam> findBySeasonAndTeam(@Param("season") Season season, @Param("team") Team team);
}
