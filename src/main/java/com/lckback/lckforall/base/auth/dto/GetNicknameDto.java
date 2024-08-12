package com.lckback.lckforall.base.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class GetNicknameDto {

	@NoArgsConstructor
	@Getter
	public static class Request {

		private String nickname;
	}
}
