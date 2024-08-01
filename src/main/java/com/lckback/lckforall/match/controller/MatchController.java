package com.lckback.lckforall.match.controller;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.match.service.MatchService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Tag(name = "Match", description = "Today's Match 관련 api")
@RestController
@RequestMapping("/match")
public class MatchController {

	private final MatchService matchService;

	@GetMapping("/today/information")
	public ResponseEntity<?> getTodayMatches() { // 오늘 경기정보 반환

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(matchService.todayMatchInfo()));
	}
}
