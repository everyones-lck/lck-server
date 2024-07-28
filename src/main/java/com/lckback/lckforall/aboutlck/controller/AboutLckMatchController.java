package com.lckback.lckforall.aboutlck.controller;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lckback.lckforall.aboutlck.dto.FindMatchesByDateDto;
import com.lckback.lckforall.aboutlck.service.AboutLckMatchService;
import com.lckback.lckforall.base.api.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController("/aboutlck/match")
@RequiredArgsConstructor
public class AboutLckMatchController {

	private final AboutLckMatchService aboutLckMatchService;

	@GetMapping("/")
	public ApiResponse<FindMatchesByDateDto.Response> aboutLckMatchByDate(
		@RequestParam("searchDate") LocalDate searchDate) {
		FindMatchesByDateDto.Parameter param = FindMatchesByDateDto.Parameter.builder().searchDate(searchDate).build();

		FindMatchesByDateDto.Response response = aboutLckMatchService.aboutMatchesByDate(param);

		return ApiResponse.createSuccess(response);
	}
}
