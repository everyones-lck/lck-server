package com.lckback.lckforall.community.dto.post;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;

import lombok.Builder;
import lombok.Getter;

public class GetPostListDto {
	@Getter
	@Builder
	public static class Parameter {
		private Pageable pageable;
		private String postType;
	}

	@Getter
	@Builder
	public static class Response {
		List<PostDetail> postDetailList;
		Boolean isLast;
	}

	@Getter
	@Builder
	public static class PostDetail {
		private Long postId;
		private String postTitle;
		private LocalDate postCreatedAt;
		private String userNickname;
		private String supportTeamName;
		private String userProfilePicture; //게시글 작성자 프로필사진
		private String thumbnailFileUrl; //post-file url
		private Integer commentCounts;
	}
}
