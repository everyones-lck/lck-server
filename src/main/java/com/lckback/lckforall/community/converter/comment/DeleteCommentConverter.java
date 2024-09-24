package com.lckback.lckforall.community.converter.comment;

import com.lckback.lckforall.community.dto.comment.DeleteCommentDto;

public class DeleteCommentConverter {

	public static DeleteCommentDto.Parameter convertParameter(String kakaoUserId, Long commentId) {
		return DeleteCommentDto.Parameter.builder()
				.kakaoUserId(kakaoUserId)
				.commentId(commentId)
				.build();
	}
}
