package com.lckback.lckforall.match.controller;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.match.dto.PogInfoDto;
import com.lckback.lckforall.match.service.PogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Tag(name = "POG", description = "POG 관련 api")
@RestController
@RequestMapping("/pog")
public class PogController {

	private final PogService pogService;

	@Operation(summary = "match-pog로 선정된 선수정보 반환 API", description = "match-pog 선수 정보를 반환합니다")
	@PostMapping("/match")
	public ResponseEntity<ApiResponse<PogInfoDto.PogResponse>> getMatchPog( // match의 pog 투표 결과 선정된 player를 반환
		@RequestHeader("Authorization") String token,
		@RequestBody PogInfoDto.MatchPogRequest request) {
		PogInfoDto.PogResponse response = pogService.findMatchPog(request.toDto());

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(response));
	}

	@Operation(summary = "set-pog로 선정된 선수정보 반환 API", description = "set-pog 선수 정보를 반환합니다")
	@PostMapping("/set")
	public ResponseEntity<ApiResponse<PogInfoDto.PogResponse>> getSetPog(// set의 pog 투표 결과 선정된 player를 반환
		@RequestHeader("Authorization") String token,
		@RequestParam("set") Integer setIndex,
		@RequestBody PogInfoDto.MatchPogRequest request) {
		PogInfoDto.PogResponse response = pogService.findSetPog(request.toDto(), setIndex);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(response));
	}
}
