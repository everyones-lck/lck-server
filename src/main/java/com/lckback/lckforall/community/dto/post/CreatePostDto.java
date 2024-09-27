package com.lckback.lckforall.community.dto.post;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class CreatePostDto {

	@Getter
	public static class Request {
		private String postType;
		private String postTitle;
		private String postContent;
	}

	@Getter
	@Builder
	public static class Parameter {
		private List<MultipartFile> files;
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
