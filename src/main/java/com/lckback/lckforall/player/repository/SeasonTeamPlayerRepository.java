package com.lckback.lckforall.player.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lckback.lckforall.player.model.Player;
import com.lckback.lckforall.player.model.SeasonTeamPlayer;

public interface SeasonTeamPlayerRepository extends JpaRepository<SeasonTeamPlayer, Long> {
	@EntityGraph(attributePaths = {"seasonTeam"})
	List<SeasonTeamPlayer> findAllByPlayer(Player player);
}
