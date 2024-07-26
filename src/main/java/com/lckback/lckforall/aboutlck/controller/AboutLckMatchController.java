package com.lckback.lckforall.aboutlck.controller;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lckback.lckforall.aboutlck.dto.AboutMatchControllerDto;
import com.lckback.lckforall.aboutlck.dto.AboutMatchServiceDto;
import com.lckback.lckforall.aboutlck.service.AboutLckMatchService;
import com.lckback.lckforall.base.api.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController("/aboutlck/match")
@RequiredArgsConstructor
public class AboutLckMatchController {

	private final AboutLckMatchService aboutLckMatchService;

	@GetMapping("/")
	public ApiResponse<AboutMatchControllerDto.Response> aboutLckMatchByDate(
		@RequestParam("searchDate") LocalDate searchDate) {
		AboutMatchServiceDto.findMatchesByDate findMatchesByDate = AboutMatchServiceDto.findMatchesByDate.builder()
			.searchDate(searchDate)
			.build();
		AboutMatchControllerDto.Response response = aboutLckMatchService.aboutMatchesByDate(findMatchesByDate);
		return ApiResponse.createSuccess(response);
	}
}
