package com.lckback.lckforall.community.controller;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.base.auth.service.AuthService;
import com.lckback.lckforall.base.setting.SwaggerPageable;
import com.lckback.lckforall.community.converter.post.CreatePostConverter;
import com.lckback.lckforall.community.converter.post.DeletePostConverter;
import com.lckback.lckforall.community.converter.post.GetPostDetailConverter;
import com.lckback.lckforall.community.converter.post.GetPostListConverter;
import com.lckback.lckforall.community.converter.post.ModifyPostConverter;
import com.lckback.lckforall.community.dto.post.CreatePostDto;
import com.lckback.lckforall.community.dto.post.DeletePostDto;
import com.lckback.lckforall.community.dto.post.GetPostDetailDto;
import com.lckback.lckforall.community.dto.post.GetPostListDto;
import com.lckback.lckforall.community.dto.post.GetPostTypeListDto;
import com.lckback.lckforall.community.dto.post.ModifyPostDto;
import com.lckback.lckforall.community.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/post")
@SecurityRequirement(name = "JWT Token")
@Tag(name = "Post", description = "게시글 관련 API")
public class PostController {

	private final PostService postService;
	private final AuthService authService;

	@GetMapping("/list")
	@SwaggerPageable
	@Operation(summary = "게시글 목록 조회")
	public ResponseEntity<ApiResponse<GetPostListDto.Response>> getPosts(
		Pageable pageable,
		@RequestParam String postType) {

		GetPostListDto.Parameter parameter = GetPostListConverter.convertParameter(postType, pageable);

		GetPostListDto.Response data = postService.findPosts(parameter);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(data));
	}

	@PostMapping("/create")
	@Operation(summary = "게시글 생성")
	public ResponseEntity<ApiResponse<CreatePostDto.Response>> createPost(
		@Parameter(hidden = true) @RequestHeader("Authorization") String token,
		@RequestPart(value = "files", required = false) List<MultipartFile> files,
		@RequestPart CreatePostDto.Request request) {
		String kakaoUserId = authService.getKakaoUserId(token);

		CreatePostDto.Parameter parameter = CreatePostConverter.convertParameter(kakaoUserId, request, files);

		CreatePostDto.Response data = postService.createPost(parameter);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(data));
	}

	@GetMapping("/type-list")
	@Operation(summary = "게시글 타입 목록 조회")
	public ResponseEntity<ApiResponse<GetPostTypeListDto.Response>> getPostTypes() {
		GetPostTypeListDto.Response data = postService.getPostTypes();

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(data));
	}

	@GetMapping("/{postId}/detail")
	@Operation(summary = "게시글 상세 조회")
	public ResponseEntity<ApiResponse<GetPostDetailDto.Response>> getPostDetail(
		@PathVariable Long postId) {

		GetPostDetailDto.Parameter parameter = GetPostDetailConverter.convertParameter(postId);

		GetPostDetailDto.Response data = postService.getPostDetail(parameter);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(data));
	}

	@DeleteMapping("/{postId}/delete")
	@Operation(summary = "게시글 삭제")
	public ResponseEntity<ApiResponse<Void>> deletePost(
		@Parameter(hidden = true) @RequestHeader("Authorization") String token,
		@PathVariable Long postId) {
		String kakaoUserId = authService.getKakaoUserId(token);

		DeletePostDto.Parameter parameter = DeletePostConverter.convertParameter(kakaoUserId, postId);

		postService.deletePost(parameter);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccessWithNoContent());
	}

	@PatchMapping("/{postId}/modify")
	@Operation(summary = "게시글 수정")
	public ResponseEntity<ApiResponse<ModifyPostDto.Response>> modifyPost(
		@Parameter(hidden = true) @RequestHeader("Authorization") String token,
		@PathVariable Long postId,
		@RequestBody ModifyPostDto.Request request) {

		String kakaoUserId = authService.getKakaoUserId(token);

		ModifyPostDto.Parameter parameter = ModifyPostConverter.convertParameter(request, postId, kakaoUserId);

		ModifyPostDto.Response data = postService.updatePost(parameter);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(data));
	}
}
