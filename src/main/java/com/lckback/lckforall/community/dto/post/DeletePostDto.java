package com.lckback.lckforall.community.dto.post;

import lombok.Builder;
import lombok.Getter;

public class DeletePostDto {

	@Getter
	@Builder
	public static class Parameter {
		private String kakaoUserId;
		private Long postId;
	}
}
