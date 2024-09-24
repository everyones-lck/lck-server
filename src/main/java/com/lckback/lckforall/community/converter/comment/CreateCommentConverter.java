package com.lckback.lckforall.community.converter.comment;

import com.lckback.lckforall.community.dto.comment.CreateCommentDto;

public class CreateCommentConverter {

	public static CreateCommentDto.Parameter convertParameter(CreateCommentDto.Request request, Long postId,
		String kakaoUserId) {
		return CreateCommentDto.Parameter.builder()
			.postId(postId)
			.content(request.getContent())
			.kakaoUserId(kakaoUserId)
			.build();
	}
}
