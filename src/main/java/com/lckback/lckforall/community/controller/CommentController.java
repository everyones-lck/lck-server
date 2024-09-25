package com.lckback.lckforall.community.controller;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.base.auth.service.AuthService;
import com.lckback.lckforall.community.converter.comment.CreateCommentConverter;
import com.lckback.lckforall.community.converter.comment.DeleteCommentConverter;
import com.lckback.lckforall.community.dto.comment.CreateCommentDto;
import com.lckback.lckforall.community.service.CommentService;
import com.lckback.lckforall.community.dto.comment.DeleteCommentDto;

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
		@RequestBody CreateCommentDto.Request request) {
		String kakaoUserId = authService.getKakaoUserId(token);

		CreateCommentDto.Parameter parameter = CreateCommentConverter.convertParameter(request, postId, kakaoUserId);

		commentService.createComment(parameter);

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

		DeleteCommentDto.Parameter parameter = DeleteCommentConverter.convertParameter(kakaoUserId, commentId);

		commentService.deleteComment(parameter);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccessWithNoContent());
	}

}
