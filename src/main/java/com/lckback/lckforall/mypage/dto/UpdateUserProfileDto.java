package com.lckback.lckforall.mypage.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Pattern;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UpdateUserProfileDto {

	@NoArgsConstructor
	@Getter
	public static class Request {

		@Nullable
		@Pattern(regexp = "^(\\S){1,10}$", message = "닉네임은 최소 1자에서 최대 10자, 공백이 없어야 합니다.")
		private String nickname;

		private boolean isDefaultImage;
	}

	@Builder
	@Getter
	public static class Response {

		private String updatedProfileImageUrl;
		private String updatedNickname;
	}
}
