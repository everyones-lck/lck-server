package com.lckback.lckforall.community.dto.post;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

public class GetPostDetailDto {

	@Getter
	@Builder
	public static class Parameter {
		private Long postId;
	}

	@Getter
	@Builder
	public static class Response {
		private String postType;
		private String writerProfileUrl;
		private String writerNickname;
		private String writerTeam;
		private String postTitle;
		private LocalDateTime postCreatedAt;
		private String content;
		private List<FileDetail> fileList;
		private List<CommentDetail> commentList;
	}

	@Getter
	@Builder
	public static class FileDetail {
		private String fileUrl;
		private Boolean isImage;
	}

	@Getter
	@Builder
	public static class CommentDetail {
		private String profileImageUrl;
		private String nickname;
		private String supportTeam;
		private String content;
		private LocalDateTime createdAt;
		private Long commentId;
	}
}
