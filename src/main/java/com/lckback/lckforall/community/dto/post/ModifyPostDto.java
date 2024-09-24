package com.lckback.lckforall.community.dto.post;

import lombok.Builder;
import lombok.Getter;

public class ModifyPostDto {

	@Getter
	@Builder
	public static class Request {
		private String postType;
		private String postTitle;
		private String postContent;
	}

	@Getter
	@Builder
	public static class Parameter {
		private Long postId;
		private String kakaoUserId;
		private String postType;
		private String postTitle;
		private String postContent;
	}

	@Getter
	@Builder
	public static class Response {
		private Long postId;
	}
}
