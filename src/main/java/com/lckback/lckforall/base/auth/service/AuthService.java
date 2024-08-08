package com.lckback.lckforall.base.auth.service;

import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.lckback.lckforall.s3.service.S3Service;
import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.base.api.error.TeamErrorCode;
import com.lckback.lckforall.base.api.error.TokenErrorCode;
import com.lckback.lckforall.base.api.error.UserErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;
import com.lckback.lckforall.base.auth.converter.AuthResponseConverter;
import com.lckback.lckforall.base.auth.dto.AuthResponseDto;
import com.lckback.lckforall.base.auth.dto.GetKakaoUserIdDto;
import com.lckback.lckforall.base.auth.dto.GetRefreshTokenDto;
import com.lckback.lckforall.base.auth.jwt.JWTUtil;
import com.lckback.lckforall.base.auth.jwt.TokenService;
import com.lckback.lckforall.base.auth.jwt.model.RefreshToken;
import com.lckback.lckforall.base.auth.jwt.repository.RefreshTokenRepository;
import com.lckback.lckforall.base.config.CustomAuthenticationToken;
import com.lckback.lckforall.team.model.Team;
import com.lckback.lckforall.team.repository.TeamRepository;
import com.lckback.lckforall.user.converter.SignupUserDataConverter;
import com.lckback.lckforall.user.dto.SignupUserDataDto;
import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.user.respository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

	private final JWTUtil jwtUtil;
	private final TokenService tokenService;
	private final UserRepository userRepository;
	private final TeamRepository teamRepository;
	private final RefreshTokenRepository refreshTokenRepository;

	private final S3Service s3Service;

	public AuthResponseDto signup(MultipartFile profileImage, SignupUserDataDto.SignupUserData signupUserData) {
		if (userRepository.existsByKakaoUserId(signupUserData.getKakaoUserId())) {
			throw new RestApiException(UserErrorCode.USER_ALREADY_EXISTS);
		}

		//String profileImageUrl = "temp"; // s3 도입 전까지 임시

		String profileImageUrl = s3Service.upload(profileImage);

		if (!profileImage.isEmpty()) {
		}

		Team team = teamRepository.findById(signupUserData.getTeamId()).orElseThrow(() -> new RestApiException(
			TeamErrorCode.NOT_EXISTS_TEAM
		));

		User user = SignupUserDataConverter.convertToUser(signupUserData, profileImageUrl, team);
		userRepository.save(user);

		String role = user.getRole().name();
		String accessToken = tokenService.createAccessToken(user.getKakaoUserId(), role);
		String refreshToken = tokenService.createRefreshToken(user.getKakaoUserId(), role);

		RefreshToken savedRefreshToken = RefreshToken.builder()
			.kakaoUserId(user.getKakaoUserId())
			.refreshToken(refreshToken)
			.build();

		refreshTokenRepository.save(savedRefreshToken);

		setAuthentication(user.getKakaoUserId(), role, accessToken);

		long currentTimestamp = System.currentTimeMillis();
		String accessTokenExpirationTime = jwtUtil.formatDate(currentTimestamp + jwtUtil.getAccessTokenExpiration());
		String refreshTokenExpirationTime = jwtUtil.formatDate(currentTimestamp + jwtUtil.getRefreshTokenExpiration());

		return AuthResponseConverter.convertToAuthResponseDto(accessToken, refreshToken, accessTokenExpirationTime, refreshTokenExpirationTime);
	}

	public AuthResponseDto login(GetKakaoUserIdDto.Request request) {

		User user = userRepository.findByKakaoUserId(request.getKakaoUserId())
			.orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_KAKAO_USER_ID));

		String role = user.getRole().name();

		// 토큰 생성
		String accessToken = tokenService.createAccessToken(request.getKakaoUserId(), role);
		String refreshToken = tokenService.createRefreshToken(request.getKakaoUserId(), role);

		RefreshToken savedRefreshToken = RefreshToken.builder()
			.kakaoUserId(user.getKakaoUserId())
			.refreshToken(refreshToken)
			.build();

		refreshTokenRepository.save(savedRefreshToken);

		setAuthentication(request.getKakaoUserId(), role, accessToken);

		long currentTimestamp = System.currentTimeMillis();
		String accessTokenExpirationTime = jwtUtil.formatDate(currentTimestamp + jwtUtil.getAccessTokenExpiration());
		String refreshTokenExpirationTime = jwtUtil.formatDate(currentTimestamp + jwtUtil.getRefreshTokenExpiration());

		return AuthResponseConverter.convertToAuthResponseDto(accessToken, refreshToken, accessTokenExpirationTime, refreshTokenExpirationTime);
	}

	public AuthResponseDto refresh(GetRefreshTokenDto.Request request) {

		jwtUtil.validateRefreshToken(request.getRefreshToken());

		RefreshToken findRefreshToken = refreshTokenRepository.findById(request.getKakaoUserId())
			.orElseThrow(() -> new RestApiException(TokenErrorCode.INVALID_REFRESH_TOKEN));

		if (!request.getRefreshToken().equals(findRefreshToken.getRefreshToken())) {

			throw new RestApiException(TokenErrorCode.INVALID_REFRESH_TOKEN);
		}

		User user = userRepository.findByKakaoUserId(request.getKakaoUserId())
			.orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_KAKAO_USER_ID));

		String role = user.getRole().name();
		String newAccessToken = tokenService.createAccessToken(request.getKakaoUserId(), role);
		String newRefreshToken = tokenService.createRefreshToken(request.getKakaoUserId(), role);

		RefreshToken savedRefreshToken = new RefreshToken(request.getKakaoUserId(), newRefreshToken);

		refreshTokenRepository.save(savedRefreshToken);

		setAuthentication(request.getKakaoUserId(), role, newAccessToken);

		long currentTimestamp = System.currentTimeMillis();
		String accessTokenExpirationTime = jwtUtil.formatDate(currentTimestamp + jwtUtil.getAccessTokenExpiration());
		String refreshTokenExpirationTime = jwtUtil.formatDate(currentTimestamp + jwtUtil.getRefreshTokenExpiration());

		return AuthResponseConverter.convertToAuthResponseDto(newAccessToken, newRefreshToken, accessTokenExpirationTime, refreshTokenExpirationTime);
	}

	public ResponseEntity<?> testToken(String token) {
		try {
			// 토큰에서 Bearer 제거
			String actualToken = token.substring(7);
			// 토큰 검증
			if (jwtUtil.isTokenExpired(actualToken)) {
				throw new RestApiException(TokenErrorCode.EXPIRED_TOKEN);
			}

			Authentication authentication = jwtUtil.getAuthentication(actualToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);

			return ResponseEntity.ok(ApiResponse.createSuccess("Token is valid"));

		} catch (Exception e) {
			throw new RestApiException(TokenErrorCode.INVALID_ACCESS_TOKEN);
		}
	}

	// 카카오 유저 아이디 가져옴
	public String getKakaoUserId(String accessToken) {
		String actualToken = accessToken.substring(7);
		return jwtUtil.getUserId(actualToken);
	}

	private void setAuthentication(String kakaoUserId, String role, String accessToken) {
		CustomAuthenticationToken authentication = new CustomAuthenticationToken(kakaoUserId, null, role, Collections.singleton(new SimpleGrantedAuthority(role.startsWith("ROLE_") ? role : "ROLE_" + role)));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}
