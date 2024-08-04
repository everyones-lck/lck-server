package com.lckback.lckforall.team.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lckback.lckforall.team.model.Season;

public interface SeasonRepository extends JpaRepository<Season, Long> {
	Optional<Season> findByName(String name);
}
