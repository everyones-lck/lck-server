package com.lckback.lckforall.vote.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lckback.lckforall.player.model.Player;
import com.lckback.lckforall.vote.model.SetPogVote;

public interface SetPogVoteRepository extends JpaRepository<SetPogVote, Long> {
	@Query("SELECT COUNT(spv) > 0 FROM SetPogVote spv WHERE spv.set.id = :setId AND spv.user.id = :userId")
	boolean existsBySetIdAndUserId(@Param("setId") Long setId, @Param("userId") Long userId);

	@Query("SELECT spv.player FROM SetPogVote spv WHERE spv.set.id = :setId AND spv.user.id = :userId")
	Optional<Player> votedPlayerBySetIdAndUserId(@Param("setId") Long setId, @Param("userId") Long userId);
}
