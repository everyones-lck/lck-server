package com.lckback.lckforall.base.auth.jwt;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";

    private final RedisTemplate<String, Object> redisTemplate;
    private final JWTUtil jwtUtil;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public TokenService(RedisTemplate<String, Object> redisTemplate, JWTUtil jwtUtil,
        @Value("${spring.jwt.access-token-expiration}") long accessTokenExpiration,
        @Value("${spring.jwt.refresh-token-expiration}") long refreshTokenExpiration) {

        this.redisTemplate = redisTemplate;
        this.jwtUtil = jwtUtil;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    // AccessToken 생성
    public String createAccessToken(String userId, String role) {

        return jwtUtil.createToken(userId, role, accessTokenExpiration);
    }

    // RefreshToken 생성
    public String createRefreshToken(String userId, String role) {

        String refreshToken = jwtUtil.createToken(userId, role, refreshTokenExpiration);
        saveRefreshToken(userId, refreshToken, refreshTokenExpiration);
        return refreshToken;
    }

    // RefreshToken Redis에 저장
    public void saveRefreshToken(String userId, String token, long durationMs) {

        redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX + userId, token, durationMs, TimeUnit.MILLISECONDS);
    }

    // Redis에서 RefreshToken 가져옴
    public String getRefreshToken(String userId) {

        return (String) redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + userId);
    }

    // Redis에 저장된 RefreshToken 유효성 검사
    public boolean validateRefreshToken(String userId, String refreshToken) {

        String storedToken = getRefreshToken(userId);
        return storedToken != null && storedToken.equals(refreshToken) && !jwtUtil.isTokenExpired(refreshToken);
    }

    // Redis에서 RefreshToken 삭제
    public void deleteRefreshToken(String userId) {

        redisTemplate.delete(REFRESH_TOKEN_PREFIX + userId);
    }
}

