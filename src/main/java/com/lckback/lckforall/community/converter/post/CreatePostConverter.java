package com.lckback.lckforall.community.converter.post;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.lckback.lckforall.community.dto.post.CreatePostDto;

public class CreatePostConverter {

	public static CreatePostDto.Parameter convertParameter(String kakaoUserId,
		CreatePostDto.Request request,
		List<MultipartFile> files) {
		return CreatePostDto.Parameter.builder()
			.kakaoUserId(kakaoUserId)
			.postType(request.getPostType())
			.postTitle(request.getPostTitle())
			.postContent(request.getPostContent())
			.files(files)
			.build();
	}
}
