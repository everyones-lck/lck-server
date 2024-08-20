package com.lckback.lckforall.community.controller;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.base.auth.service.AuthService;
import com.lckback.lckforall.community.dto.CommentDto;
import com.lckback.lckforall.community.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
@SecurityRequirement(name = "JWT Token")
@Tag(name = "Comment", description = "댓글 관련 API")
public class CommentController {

	private final AuthService authService;
	private final CommentService commentService;

	@Operation(summary = "댓글 생성")
	@PostMapping("/{postId}/create")
	public ResponseEntity<ApiResponse<Void>> createComment(
		@Parameter(hidden = true) @RequestHeader("Authorization") String token,
		@PathVariable Long postId,
		@RequestBody CommentDto.createCommentRequest request) {
		String kakaoUserId = authService.getKakaoUserId(token);
		commentService.createComment(postId, request, kakaoUserId);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccessWithNoContent());
	}

	@Operation(summary = "댓글 삭제")
	@DeleteMapping("/{commentId}/delete")
	public ResponseEntity<ApiResponse<Void>> deleteComment(
		@Parameter(hidden = true) @RequestHeader("Authorization") String token,
		@PathVariable Long commentId
	) {
		String kakaoUserId = authService.getKakaoUserId(token);
		commentService.deleteComment(commentId, kakaoUserId);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccessWithNoContent());
	}

}
