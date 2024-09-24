package com.lckback.lckforall.community.converter.post;

import com.lckback.lckforall.community.dto.post.DeletePostDto;

public class DeletePostConverter {

	public static DeletePostDto.Parameter convertParameter(String kakaoUserId, Long postId) {
		return DeletePostDto.Parameter.builder()
				.kakaoUserId(kakaoUserId)
				.postId(postId)
				.build();
	}
}
