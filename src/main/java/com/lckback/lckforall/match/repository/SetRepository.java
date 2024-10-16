package com.lckback.lckforall.match.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lckback.lckforall.match.model.Match;
import com.lckback.lckforall.match.model.Set;

public interface SetRepository extends JpaRepository<Set, Long> {
	@Query("SELECT s FROM Set s WHERE s.setIndex = :setIndex AND s.match.id = :matchId")
	Optional<Set> findBySetIndexAndMatchId(Integer setIndex, Long matchId);

	List<Set> findByMatchId(Long matchId);
}
