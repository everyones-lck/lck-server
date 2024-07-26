package com.lckback.lckforall.aboutlck.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lckback.lckforall.match.model.Match;

public interface MatchRepository extends JpaRepository<Match, Long> {
	List<Match> findMatchesByMatchDateBetween(LocalDateTime start, LocalDateTime end);
}
