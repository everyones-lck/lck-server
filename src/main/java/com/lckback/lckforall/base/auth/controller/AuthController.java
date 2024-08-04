package com.lckback.lckforall.base.auth.controller;

import java.util.Collections;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.base.api.error.UserErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;
import com.lckback.lckforall.base.auth.converter.AuthResponseConverter;
import com.lckback.lckforall.base.auth.dto.AuthResponseDto;
import com.lckback.lckforall.base.auth.jwt.JWTUtil;
import com.lckback.lckforall.base.auth.jwt.TokenService;
import com.lckback.lckforall.user.converter.SignupUserDataConverter;
import com.lckback.lckforall.user.dto.SignupUserDataDto;
import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.user.respository.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JWTUtil jwtUtil;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public AuthController(JWTUtil jwtUtil, TokenService tokenService, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    // 회원가입 후 자동으로 로그인 된다고 생각해 성공 시 바로 jwt 발급
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupUserDataDto.SignupUserData signupUserData) {

        if (userRepository.findByKakaoUserId(signupUserData.getKakaoUserId()).isPresent()) {

            throw new RestApiException(UserErrorCode.USER_ALREADY_EXISTS);
        }

        User user = SignupUserDataConverter.convertToUser(signupUserData);
        User savedUser = userRepository.save(user);

        String role = savedUser.getRole().name();
        String accessToken = tokenService.createAccessToken(user.getKakaoUserId(), role);
        String refreshToken = tokenService.createRefreshToken(user.getKakaoUserId(), role);

        // 회원가입 후 자동으로 로그인 되도록 SecurityContextHolder에 인증 정보 설정
        setAuthentication(user.getKakaoUserId(), role, accessToken);

        long currentTimestamp = System.currentTimeMillis();
        String accessTokenExpirationTime = jwtUtil.formatDate(currentTimestamp + jwtUtil.getAccessTokenExpiration());
        String refreshTokenExpirationTime = jwtUtil.formatDate(currentTimestamp + jwtUtil.getRefreshTokenExpiration());

        AuthResponseDto authResponseDto = AuthResponseConverter.convertToAuthResponseDto(accessToken, refreshToken, accessTokenExpirationTime, refreshTokenExpirationTime);

        return ResponseEntity.status(201).body(ApiResponse.createSuccess(authResponseDto));
    }

    // 로그인 로직
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String kakaoUserId) {

        Optional<User> userOptional = userRepository.findByKakaoUserId(kakaoUserId);

        if (userOptional.isEmpty()) {

            throw new RestApiException(UserErrorCode.INVALID_KAKAO_USER_ID);
        }

        User user = userOptional.get();
        String role = user.getRole().name();

        // SecurityContext에서 현재 인증 정보 가져오기
        SecurityContext context = SecurityContextHolder.getContext();
        String accessToken = null;

        // 기존 인증이 있는지 확인
        if (context.getAuthentication() != null &&
            context.getAuthentication().getPrincipal().equals(kakaoUserId)) {
            // 인증에서 현재 accessToken 가져오기
            accessToken = (String) context.getAuthentication().getCredentials();

            // 현재 accessToken 유효성 검사
            if (jwtUtil.isTokenExpired(accessToken)) {
                accessToken = tokenService.createAccessToken(kakaoUserId, role);
                setAuthentication(kakaoUserId, role, accessToken);
            }

        } else {
            // 기존 인증이 없으면 새 accessToken 생성
            accessToken = tokenService.createAccessToken(kakaoUserId, role);
            setAuthentication(kakaoUserId, role, accessToken);
        }

        String refreshToken = tokenService.getRefreshToken(kakaoUserId);
        if (refreshToken == null || !tokenService.validateRefreshToken(kakaoUserId, refreshToken)) {
            refreshToken = tokenService.createRefreshToken(kakaoUserId, role);
        }

        long currentTimestamp = System.currentTimeMillis();
        String accessTokenExpirationTime = jwtUtil.formatDate(currentTimestamp + jwtUtil.getAccessTokenExpiration());
        String refreshTokenExpirationTime = jwtUtil.formatDate(currentTimestamp + jwtUtil.getRefreshTokenExpiration());

        AuthResponseDto authResponseDto = AuthResponseConverter.convertToAuthResponseDto(accessToken, refreshToken, accessTokenExpirationTime, refreshTokenExpirationTime);

        return ResponseEntity.ok(ApiResponse.createSuccess(authResponseDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestParam String kakaoUserId) {

        String refreshToken = tokenService.getRefreshToken(kakaoUserId);
        if (refreshToken == null || !tokenService.validateRefreshToken(kakaoUserId, refreshToken)) {
            throw new RestApiException(UserErrorCode.INVALID_REFRESH_TOKEN);
        }

        Optional<User> userOptional = userRepository.findByKakaoUserId(kakaoUserId);
        if (userOptional.isEmpty()) {
            throw new RestApiException(UserErrorCode.INVALID_KAKAO_USER_ID);
        }

        User user = userOptional.get();
        String role = user.getRole().name();
        String newAccessToken = tokenService.createAccessToken(kakaoUserId, role);

        // RefreshToken 갱신 시 반환
        String newRefreshToken = refreshToken;
        if (jwtUtil.isTokenExpired(refreshToken)) {
            newRefreshToken = tokenService.createRefreshToken(kakaoUserId, role);
        }

        // SecurityContextHolder에 인증 정보 설정
        setAuthentication(kakaoUserId, role, newAccessToken);

        long currentTimestamp = System.currentTimeMillis();
        String accessTokenExpirationTime = jwtUtil.formatDate(currentTimestamp + jwtUtil.getAccessTokenExpiration());
        String refreshTokenExpirationTime = jwtUtil.formatDate(currentTimestamp + jwtUtil.getRefreshTokenExpiration());

        AuthResponseDto authResponseDto = AuthResponseConverter.convertToAuthResponseDto(newAccessToken, newRefreshToken, accessTokenExpirationTime, refreshTokenExpirationTime);

        return ResponseEntity.ok(ApiResponse.createSuccess(authResponseDto));
    }


    private void setAuthentication(String kakaoUserId, String role, String accessToken) {

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.startsWith("ROLE_") ? role : "ROLE_" + role);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(kakaoUserId, accessToken, Collections.singleton(authority));
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

}
