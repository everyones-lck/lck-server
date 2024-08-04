package com.lckback.lckforall.viewing.repository;

import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.viewing.model.ViewingParty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ViewingPartyRepository extends JpaRepository<ViewingParty, Long> {
    Page<ViewingParty> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Optional<ViewingParty> findByIdAndUser(Long viewingPartyId, User user);
}
