package com.lckback.lckforall.viewing.repository;

import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.viewing.model.ViewingParty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewingPartyRepository extends JpaRepository<ViewingParty, Long> {

	Page<ViewingParty> findAllByOrderByCreatedAtDesc(Pageable pageable);

	Page<ViewingParty> findByUser(User user, Pageable pageable);

}
