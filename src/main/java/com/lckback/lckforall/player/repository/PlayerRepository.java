package com.lckback.lckforall.player.repository;

import java.util.Optional;

import com.lckback.lckforall.base.type.PlayerRole;
import com.lckback.lckforall.player.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
	Optional<Player> findByRole(PlayerRole role);
}
