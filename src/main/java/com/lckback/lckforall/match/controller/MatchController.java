package com.lckback.lckforall.match.controller;

import java.util.List;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.match.dto.MatchInfoDto;
import com.lckback.lckforall.match.service.MatchService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Tag(name = "Match", description = "Today's Match 관련 api")
@RestController
@RequestMapping("/match")
public class MatchController {

	private final MatchService matchService;

	@GetMapping("/today/information")
	public ResponseEntity<?> getTodayMatches(@RequestHeader("Authorization") String token) { // 오늘 경기정보 반환
		List<MatchInfoDto.TodayMatchResponse> responses = matchService.todayMatchInfo();

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(responses));
	}
}
