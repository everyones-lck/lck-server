package com.lckback.lckforall.base.auth.service;

import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lckback.lckforall.base.api.error.TokenErrorCode;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final JWTUtil jwtUtil;
	private final TokenService tokenService;
	private final UserRepository userRepository;

	public AuthResponseDto signup(MultipartFile profileImage, SignupUserDataDto.SignupUserData signupUserData) {
		if (userRepository.existsByKakaoUserId(signupUserData.getKakaoUserId())) {
			throw new RestApiException(UserErrorCode.USER_ALREADY_EXISTS);
		}

		String profileImageUrl = "temp"; // s3 도입 전까지 임시

		if (profileImage != null && !profileImage.isEmpty()) {
			// 프로필 이미지를 저장하고 URL을 얻는 로직 추가 필요
			// 예: profileImageUrl = s3Service.uploadFile(profileImage);
		}

		User user = SignupUserDataConverter.convertToUser(signupUserData, profileImageUrl);
		userRepository.save(user);

		String role = user.getRole().name();
		String accessToken = tokenService.createAccessToken(user.getKakaoUserId(), role);
		String refreshToken = tokenService.createRefreshToken(user.getKakaoUserId(), role);

		setAuthentication(user.getKakaoUserId(), role, accessToken);

		long currentTimestamp = System.currentTimeMillis();
		String accessTokenExpirationTime = jwtUtil.formatDate(currentTimestamp + jwtUtil.getAccessTokenExpiration());
		String refreshTokenExpirationTime = jwtUtil.formatDate(currentTimestamp + jwtUtil.getRefreshTokenExpiration());

		return AuthResponseConverter.convertToAuthResponseDto(accessToken, refreshToken, accessTokenExpirationTime, refreshTokenExpirationTime);
	}

	public AuthResponseDto login(String kakaoUserId) {

		User user = userRepository.findByKakaoUserId(kakaoUserId)
			.orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_KAKAO_USER_ID));

		String role = user.getRole().name();

		SecurityContext context = SecurityContextHolder.getContext();
		String accessToken = null;

		if (context.getAuthentication() != null && context.getAuthentication().getPrincipal().equals(kakaoUserId)) {
			accessToken = (String) context.getAuthentication().getCredentials();

			if (jwtUtil.isTokenExpired(accessToken)) {
				accessToken = tokenService.createAccessToken(kakaoUserId, role);
				setAuthentication(kakaoUserId, role, accessToken);
			}
		} else {
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

		return AuthResponseConverter.convertToAuthResponseDto(accessToken, refreshToken, accessTokenExpirationTime, refreshTokenExpirationTime);
	}

	public AuthResponseDto refresh(String kakaoUserId) {

		String refreshToken = tokenService.getRefreshToken(kakaoUserId);
		if (refreshToken == null || !tokenService.validateRefreshToken(kakaoUserId, refreshToken)) {
			throw new RestApiException(TokenErrorCode.INVALID_REFRESH_TOKEN);
		}

		User user = userRepository.findByKakaoUserId(kakaoUserId)
			.orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_KAKAO_USER_ID));

		String role = user.getRole().name();
		String newAccessToken = tokenService.createAccessToken(kakaoUserId, role);

		String newRefreshToken = refreshToken;
		if (jwtUtil.isTokenExpired(refreshToken)) {
			newRefreshToken = tokenService.createRefreshToken(kakaoUserId, role);
		}

		setAuthentication(kakaoUserId, role, newAccessToken);

		long currentTimestamp = System.currentTimeMillis();
		String accessTokenExpirationTime = jwtUtil.formatDate(currentTimestamp + jwtUtil.getAccessTokenExpiration());
		String refreshTokenExpirationTime = jwtUtil.formatDate(currentTimestamp + jwtUtil.getRefreshTokenExpiration());

		return AuthResponseConverter.convertToAuthResponseDto(newAccessToken, newRefreshToken, accessTokenExpirationTime, refreshTokenExpirationTime);
	}

	private void setAuthentication(String kakaoUserId, String role, String accessToken) {

		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.startsWith("ROLE_") ? role : "ROLE_" + role);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(kakaoUserId, accessToken, Collections.singleton(authority));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
