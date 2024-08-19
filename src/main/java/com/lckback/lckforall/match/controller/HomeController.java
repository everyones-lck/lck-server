package com.lckback.lckforall.match.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.match.dto.MatchInfoDto;
import com.lckback.lckforall.match.service.HomeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Tag(name = "Home", description = "Home 화면 관련 api")
@RestController
@RequestMapping("/home")
@SecurityRequirement(name = "JWT Token")
public class HomeController {
	private final HomeService homeService;

	@Operation(summary = "오늘 경기 정보 + 최근 종료 경기 정보 API", description = "오늘 경기 정보와 최근 종료 경기 결과를 반환합니다")
	@GetMapping("/today/information")
	public ResponseEntity<ApiResponse<MatchInfoDto.HomePageResponse>> getTodayMatches(@RequestHeader("Authorization") String token) {
		MatchInfoDto.HomePageResponse response = homeService.getHomePageInformation();

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(response));
	}
}
