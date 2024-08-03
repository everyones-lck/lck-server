package com.lckback.lckforall.match.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.match.dto.MatchInfoDto;
import com.lckback.lckforall.match.service.HomeService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Tag(name = "Home", description = "Home 화면 관련 api")
@RestController
@RequestMapping("/home")
public class HomeController {
	private final HomeService homeService;

	@GetMapping("/today/information")
	public ResponseEntity<?> getTodayMatches() {
		MatchInfoDto.HomePageResponse response = homeService.getHomePageInformation();

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(response));
	}
}
