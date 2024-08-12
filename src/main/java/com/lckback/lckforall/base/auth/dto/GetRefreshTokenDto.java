package com.lckback.lckforall.base.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class GetRefreshTokenDto {


	@NoArgsConstructor
	@Getter
	public static class Request {

		private String kakaoUserId;
		private String refreshToken;
	}
}
