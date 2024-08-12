package com.lckback.lckforall.base.auth.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lckback.lckforall.base.auth.jwt.model.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {


}
