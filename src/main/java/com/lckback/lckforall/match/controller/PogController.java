package com.lckback.lckforall.match.controller;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.match.dto.PogInfoDto;
import com.lckback.lckforall.match.service.PogService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Tag(name = "POG", description = "POG 관련 api")
@RestController
@RequestMapping("/pog")
public class PogController {

	private final PogService pogService;

	@PostMapping("/match")
	public ResponseEntity<?> getMatchPog( // match의 pog 투표 결과 선정된 player를 반환
		@RequestBody PogInfoDto.MatchPogRequest request) {
		PogInfoDto.PogResponse response = pogService.findMatchPog(request.toDto());

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(response));
	}

	@PostMapping("/set")
	public ResponseEntity<?> getSetPog( // match의 pog 투표 결과 선정된 player를 반환
		@RequestParam("set") Integer setIndex,
		@RequestBody PogInfoDto.MatchPogRequest request) {
		PogInfoDto.PogResponse response = pogService.findSetPog(request.toDto(), setIndex);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(response));
	}
}
