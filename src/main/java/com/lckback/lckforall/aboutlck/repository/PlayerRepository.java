package com.lckback.lckforall.aboutlck.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lckback.lckforall.player.model.Player;

@Repository("aboutLckPlayerRepository")
public interface PlayerRepository extends JpaRepository<Player, Long> {
}
