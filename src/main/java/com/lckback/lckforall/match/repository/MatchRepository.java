package com.lckback.lckforall.match.repository;

import com.lckback.lckforall.match.model.Match;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository("match")
public interface MatchRepository extends JpaRepository<Match, Long> {
    @EntityGraph(attributePaths = {"team1", "team2"})
    List<Match> findMatchesByMatchDateBetween(LocalDateTime start, LocalDateTime end);
}