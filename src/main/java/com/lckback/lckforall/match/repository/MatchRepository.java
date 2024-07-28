package com.lckback.lckforall.match.repository;

import com.lckback.lckforall.match.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    @Query("SELECT m FROM Match m WHERE FUNCTION('DATE', m.matchDate) = FUNCTION('DATE', :today)")
    List<Match> findMatchesByDate(@Param("today") LocalDateTime today);
}