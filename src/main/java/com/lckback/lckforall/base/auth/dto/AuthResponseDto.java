package com.lckback.lckforall.base.auth.dto;

import lombok.Getter;

@Getter
public class AuthResponseDto {

	private final String accessToken;
	private final String refreshToken;
	private final String accessTokenExpirationTime;
	private final String refreshTokenExpirationTime;
	private final String nickName;

	public AuthResponseDto(String accessToken, String refreshToken, String accessTokenExpirationTime, String refreshTokenExpirationTime, String nickName) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.accessTokenExpirationTime = accessTokenExpirationTime;
		this.refreshTokenExpirationTime = refreshTokenExpirationTime;
        this.nickName = nickName;
    }


}
