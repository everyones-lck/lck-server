package com.lckback.lckforall.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lckback.lckforall.player.model.Player;
import com.lckback.lckforall.vote.model.MatchPogVote;

public interface MatchPogVoteRepository extends JpaRepository<MatchPogVote,Long> {

	@Query("SELECT COUNT(mpv) > 0 FROM MatchPogVote mpv WHERE mpv.match.id = :matchId AND mpv.user.id = :userId")
	boolean existsByMatchIdAndUserId(@Param("matchId") Long matchId, @Param("userId") Long userId);

	@Query("SELECT mpv.player FROM MatchPogVote mpv WHERE mpv.match.id = :matchId AND mpv.user.id = :userId")
	Player votedPlayerByMatchIdAndUserId(@Param("matchId") Long matchId, @Param("userId") Long userId);
}
