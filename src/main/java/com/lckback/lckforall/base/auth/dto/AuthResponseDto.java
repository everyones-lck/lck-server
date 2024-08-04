package com.lckback.lckforall.base.auth.dto;

import lombok.Getter;

@Getter
public class AuthResponseDto {

	private final String accessToken;
	private final String refreshToken;
	private final String accessTokenExpirationTime;
	private final String refreshTokenExpirationTime;

	public AuthResponseDto(String accessToken, String refreshToken, String accessTokenExpirationTime, String refreshTokenExpirationTime) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.accessTokenExpirationTime = accessTokenExpirationTime;
		this.refreshTokenExpirationTime = refreshTokenExpirationTime;
	}
}
