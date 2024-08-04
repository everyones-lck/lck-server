package com.lckback.lckforall.aboutlck.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lckback.lckforall.player.model.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
