package com.lckback.lckforall.base.auth.converter;

import com.lckback.lckforall.base.auth.dto.AuthResponseDto;

public class AuthResponseConverter {

	// accessToken과 refreshToken을 받아 AuthResponseDto 객체로 변환하는 역할
	public static AuthResponseDto convertToAuthResponseDto(String accessToken, String refreshToken, String accessTokenExpirationTime, String refreshTokenExpirationTime, String nickName) {

		return new AuthResponseDto(accessToken, refreshToken, accessTokenExpirationTime, refreshTokenExpirationTime, nickName);
	}

}
