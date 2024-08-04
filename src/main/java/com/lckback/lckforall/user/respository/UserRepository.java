package com.lckback.lckforall.user.respository;

import java.util.Optional;

import com.lckback.lckforall.user.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByKakaoUserId(String kakaoUserId);
	boolean existsByNickname(String nickname);

}
