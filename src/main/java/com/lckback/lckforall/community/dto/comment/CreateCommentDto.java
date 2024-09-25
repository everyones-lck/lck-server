package com.lckback.lckforall.community.dto.comment;

import lombok.Builder;
import lombok.Getter;

public class CreateCommentDto {

	@Getter
	@Builder
	public static class Request {
		private String content;
	}

	@Getter
	@Builder
	public static class Parameter {
		private Long postId;
		private String content;
		private String kakaoUserId;
	}
}
