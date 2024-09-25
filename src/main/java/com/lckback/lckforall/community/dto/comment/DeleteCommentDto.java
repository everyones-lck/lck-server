package com.lckback.lckforall.community.dto.comment;

import lombok.Builder;
import lombok.Getter;

public class DeleteCommentDto {

	@Getter
	@Builder
	public static class Parameter {
		private Long commentId;
		private String kakaoUserId;
	}
}
