package com.lckback.lckforall.community.controller;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.base.auth.service.AuthService;
import com.lckback.lckforall.base.setting.SwaggerPageable;
import com.lckback.lckforall.community.dto.PostDto;
import com.lckback.lckforall.community.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
@SecurityRequirement(name = "JWT Token")
@Tag(name = "Post", description = "게시글 관련 API")
public class PostController {

	private final PostService postService;
	private final AuthService authService;

	@GetMapping("/list")
	@SwaggerPageable
	@Operation(summary = "게시글 목록 조회")
	public ResponseEntity<ApiResponse<PostDto.PostListResponse>> getPosts(
		Pageable pageable,
		@RequestParam String postType) {

		PostDto.PostListResponse data = postService.findPosts(pageable, postType);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(data));
	}

	@PostMapping("/create")
	@Operation(summary = "게시글 생성")
	public ResponseEntity<ApiResponse<PostDto.CreatePostResponse>> createPost(
		@RequestPart(value = "files", required = false) List<MultipartFile> files,
		@RequestPart @Valid PostDto.CreatePostRequest request,
		@Parameter(hidden = true) @RequestHeader("Authorization") String token) {
		String kakaoUserId = authService.getKakaoUserId(token);
		PostDto.CreatePostResponse response = postService.createPost(files, request, kakaoUserId);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(response));
	}

	@GetMapping("/type-list")
	@Operation(summary = "게시글 타입 목록 조회")
	public ResponseEntity<ApiResponse<PostDto.PostTypeListResponse>> getPostTypes() {
		PostDto.PostTypeListResponse response = postService.getPostTypes();

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(response));
	}

	@GetMapping("/{postId}/detail")
	@Operation(summary = "게시글 상세 조회")
	public ResponseEntity<ApiResponse<PostDto.PostDetailResponse>> getPostDetail(
		@PathVariable Long postId) {
		PostDto.PostDetailResponse response = postService.getPostDetail(postId);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(response));
	}

	@DeleteMapping("/{postId}/delete")
	@Operation(summary = "게시글 삭제")
	public ResponseEntity<ApiResponse<Void>> deletePost(
		@Parameter(hidden = true) @RequestHeader("Authorization") String token,
		@PathVariable Long postId) {
		String kakaoUserId = authService.getKakaoUserId(token);
		postService.deletePost(postId, kakaoUserId);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccessWithNoContent());
	}

	@PatchMapping("/{postId}/modify")
	@Operation(summary = "게시글 수정")
	public ResponseEntity<ApiResponse<PostDto.modifyPostResponse>> modifyPost(
		@Parameter(hidden = true) @RequestHeader("Authorization") String token,
		@PathVariable Long postId,
		@RequestBody PostDto.PostModifyRequest request) {

		String kakaoUserId = authService.getKakaoUserId(token);
		postService.updatePost(request, postId, kakaoUserId);

		PostDto.modifyPostResponse response = postService.updatePost(request, postId, kakaoUserId);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(response));
	}
}
