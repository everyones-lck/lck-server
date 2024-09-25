package com.lckback.lckforall.user.respository;

import java.util.Optional;

import com.lckback.lckforall.user.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByKakaoUserId(String kakaoUserId);
	boolean existsByNickname(String nickname);
	boolean existsByKakaoUserId(String kakaoUserId);

	@Query("SELECT u FROM User u JOIN FETCH u.comments WHERE u.kakaoUserId = :kakaoUserId")
	Optional<User> findByKakaoUserIdWithComments(String kakaoUserId);

}
