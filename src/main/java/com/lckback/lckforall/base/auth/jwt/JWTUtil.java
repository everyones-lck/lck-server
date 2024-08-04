package com.lckback.lckforall.base.auth.jwt;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lckback.lckforall.base.api.error.TokenErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;

import io.jsonwebtoken.Jwts;
import lombok.Getter;

@Component
@Getter
public class JWTUtil {

    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret,
        @Value("${spring.jwt.access-token-expiration}") long accessTokenExpiration,
        @Value("${spring.jwt.refresh-token-expiration}") long refreshTokenExpiration) {

        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    // 토큰에서 user_id 추출
    public String getUserId(String token) {

        try {
            return Jwts.parser().verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
        } catch (Exception e) {

            throw new RestApiException(TokenErrorCode.INVALID_ACCESS_TOKEN);
        }
    }

    // 토큰에서 role 추출
    public String getRole(String token) {

        try {
            return Jwts.parser().verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
        } catch (Exception e) {

            throw new RestApiException(TokenErrorCode.INVALID_ACCESS_TOKEN);
        }
    }

    // 토큰 만료 여부 확인
    public boolean isTokenExpired(String token) {

        // 토큰이 null이거나 빈 문자열이면 만료된 것으로 처리
        if (token == null || token.trim().isEmpty()) {

            throw new RestApiException(TokenErrorCode.INVALID_ACCESS_TOKEN);
        }

        try {
            return Jwts.parser().verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
        } catch (Exception e) {

            throw new RestApiException(TokenErrorCode.EXPIRED_ACCESS_TOKEN);
        }

    }

    // 토큰 생성
    public String createToken(String userId, String role, Long expiredMs) {
        return Jwts.builder()
            .claim("sub", userId)
            .claim("role", role)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expiredMs))
            .signWith(secretKey)
            .compact();
    }

    // 토큰 검증
    public boolean validateToken(String token) {

        // 토큰이 null이거나 빈 문자열이면 만료된 것으로 처리
        if (token == null || token.trim().isEmpty()) {

            throw new RestApiException(TokenErrorCode.INVALID_ACCESS_TOKEN);
        }

        try {
            Jwts.parser().verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
            return true;

        } catch (Exception e) {

            throw new RestApiException(TokenErrorCode.INVALID_ACCESS_TOKEN);
        }

    }

    public String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(timestamp));
    }

}
