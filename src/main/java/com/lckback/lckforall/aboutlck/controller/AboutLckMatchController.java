package com.lckback.lckforall.aboutlck.controller;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lckback.lckforall.aboutlck.dto.match.FindMatchesByDateDto;
import com.lckback.lckforall.aboutlck.service.AboutLckMatchService;
import com.lckback.lckforall.base.api.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/aboutlck/match")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT Token")
@Tag(name = "AboutLckMatch", description = "AboutLck 경기 정보 조회 API")
public class AboutLckMatchController {

	private final AboutLckMatchService aboutLckMatchService;

	@Operation(summary = "경기 정보 조회 API", description = "특정 날짜의 경기 정보를 조회합니다.")
	@GetMapping
	public ApiResponse<FindMatchesByDateDto.Response> findMatchInformationByDate(
		@RequestParam("searchDate") LocalDate searchDate) {
		FindMatchesByDateDto.Parameter param = FindMatchesByDateDto.Parameter.builder().searchDate(searchDate).build();

		FindMatchesByDateDto.Response response = aboutLckMatchService.findMatchInformationByDate(param);

		return ApiResponse.createSuccess(response);
	}
}
