package com.lckback.lckforall.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lckback.lckforall.team.model.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
