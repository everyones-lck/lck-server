package com.lckback.lckforall.match.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.match.dto.MatchInfoDto;
import com.lckback.lckforall.match.service.HomeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Tag(name = "Home", description = "Home 화면 관련 api")
@RestController
@RequestMapping("/home")
public class HomeController {
	private final HomeService homeService;

	@Operation(summary = "홈화면에 필요한 정보를 조회 API", description = "오늘 경기정보와 최근 경기 결과를 조회합니다")
	@GetMapping("/today/information")
	public ResponseEntity<ApiResponse<MatchInfoDto.HomePageResponse>> getTodayMatches(@RequestHeader("Authorization") String token) {
		MatchInfoDto.HomePageResponse response = homeService.getHomePageInformation();

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(response));
	}
}
