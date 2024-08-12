package com.lckback.lckforall.viewing.repository;

import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.viewing.model.Participate;
import com.lckback.lckforall.viewing.model.ViewingParty;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipateRepository extends JpaRepository<Participate, Long> {

    Optional<Participate> findByUserAndViewingParty(User user, ViewingParty viewingParty);

    Page<Participate> findByUser(User user, Pageable pageable);

    Optional<Participate> findByViewingPartyAndUser(ViewingParty viewingParty, User user);
}
