package com.lckback.lckforall.aboutlck.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lckback.lckforall.match.model.Match;

@Repository("aboutLck")
public interface MatchRepository extends JpaRepository<Match, Long> {
	@EntityGraph(attributePaths = {"team1", "team2"})
	List<Match> findMatchesByMatchDateBetween(LocalDateTime start, LocalDateTime end);
}
