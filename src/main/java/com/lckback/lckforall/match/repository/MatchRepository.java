package com.lckback.lckforall.match.repository;

import com.lckback.lckforall.base.type.MatchResult;
import com.lckback.lckforall.match.model.Match;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository("match")
public interface MatchRepository extends JpaRepository<Match, Long> {
	@EntityGraph(attributePaths = {"team1", "team2"})
	List<Match> findMatchesByMatchDateBetween(LocalDateTime start, LocalDateTime end);

	@Query("SELECT m FROM Match m WHERE m.matchResult <> :notFinished ORDER BY m.matchDate DESC")
	List<Match> findRecentMatches(@Param("notFinished") MatchResult notFinished, Pageable pageable);
}