package com.lckback.lckforall.community.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class CreateCommentDto {

	@Getter
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
