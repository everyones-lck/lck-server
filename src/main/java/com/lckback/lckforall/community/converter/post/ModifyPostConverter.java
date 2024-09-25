package com.lckback.lckforall.community.converter.post;

import com.lckback.lckforall.community.dto.post.ModifyPostDto;

public class ModifyPostConverter {

	public static ModifyPostDto.Parameter convertParameter(ModifyPostDto.Request request, Long postId, String kakaoUserId) {
		return ModifyPostDto.Parameter.builder()
				.postId(postId)
				.kakaoUserId(kakaoUserId)
				.postType(request.getPostType())
				.postTitle(request.getPostTitle())
				.postContent(request.getPostContent())
				.build();
	}

	public static ModifyPostDto.Response convertResponse(Long postId) {
		return ModifyPostDto.Response.builder()
				.postId(postId)
				.build();
	}
}
