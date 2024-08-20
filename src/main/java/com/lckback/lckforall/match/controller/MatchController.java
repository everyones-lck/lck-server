package com.lckback.lckforall.match.controller;

import java.util.List;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.match.dto.MatchInfoDto;
import com.lckback.lckforall.match.service.MatchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Tag(name = "Match", description = "Today's Match 관련 api")
@RestController
@RequestMapping("/match")
@SecurityRequirement(name = "JWT Token")
public class MatchController {

	private final MatchService matchService;

	@Operation(summary = "오늘의 경기 정보 API", description = "오늘의 경기 정보를 반환합니다")
	@GetMapping("/today/information")
	public ResponseEntity<ApiResponse<MatchInfoDto.Response>> getTodayMatches(@RequestHeader("Authorization") String token) { // 오늘 경기정보 반환
		MatchInfoDto.Response responses = matchService.todayMatchInfo();

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(responses));
	}

	@Operation(summary = "해당 match의 set 수 리턴", description = "해당 match의 set수를 리턴합니다")
	@GetMapping("/set-count")
	public ResponseEntity<ApiResponse<MatchInfoDto.setCountResponse>> getTodayMatches
		(@RequestHeader("Authorization") String token,
		@RequestParam("match-id") Long matchId) { // 오늘 경기정보 반환
		MatchInfoDto.setCountResponse response = matchService.getSetCount(matchId);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(response));
	}

}
