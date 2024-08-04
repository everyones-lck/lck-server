package com.lckback.lckforall.user.respository;

import com.lckback.lckforall.user.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByNickname(String nickname);

}
