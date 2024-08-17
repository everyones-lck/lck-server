package com.lckback.lckforall.report.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.base.auth.service.AuthService;
import com.lckback.lckforall.report.service.CommentReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report/comment")
@Tag(name = "Report - Comment", description = "댓글 신고 관련 API")
@SecurityRequirement(name = "JWT Token")
public class CommentReportController {

	private final AuthService authService;
	private final CommentReportService commentReportService;

	@PostMapping("{commentId}/create")
	@Operation(summary = "댓글 신고 API", description = "댓글을 신고합니다.")
	public ApiResponse<Void> createCommentReport(
		@Parameter(hidden = true) @RequestHeader("Authorization") String token,
		@PathVariable("commentId") Long commentId
	) {
		String kakaoUserId = authService.getKakaoUserId(token);

		commentReportService.createCommentReport(kakaoUserId, commentId);

		return ApiResponse.createSuccessWithNoContent();
	}
}
