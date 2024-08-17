package com.lckback.lckforall.report.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.base.auth.service.AuthService;
import com.lckback.lckforall.report.service.PostReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report/post")
@Tag(name = "Report - Post", description = "게시글 신고 관련 API")
@SecurityRequirement(name = "JWT Token")
public class PostReportController {

	private final AuthService authService;
	private final PostReportService postReportService;

	@PostMapping("{postId}/create")
	@Operation(summary = "게시글 신고 API", description = "게시글을 신고합니다.")
	public ApiResponse<Void> createPostReport(
		@Parameter(hidden = true) @RequestHeader("Authorization") String token,
		@PathVariable("postId") Long postId
	) {
		String kakaoUserId = authService.getKakaoUserId(token);

		postReportService.createPostReport(kakaoUserId, postId);

		return ApiResponse.createSuccessWithNoContent();
	}
}
