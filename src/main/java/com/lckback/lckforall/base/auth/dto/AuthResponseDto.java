package com.lckback.lckforall.base.auth.dto;

public class AuthResponseDto {

	private final String accessToken;
	private final String refreshToken;

	public AuthResponseDto(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}
}
