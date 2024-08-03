package com.lckback.lckforall.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lckback.lckforall.vote.model.MatchVote;

public interface MatchVoteRepository extends JpaRepository<MatchVote, Long> {
	@Query("SELECT COUNT(mv) > 0 FROM MatchVote mv WHERE mv.match.id = :matchId AND mv.user.id = :userId")
	boolean existsByMatchIdAndUserId(@Param("matchId") Long matchId, @Param("userId") Long userId);
}