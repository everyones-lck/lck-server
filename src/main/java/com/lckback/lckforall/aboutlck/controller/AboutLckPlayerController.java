package com.lckback.lckforall.aboutlck.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lckback.lckforall.aboutlck.dto.player.FindPlayerInformationDto;
import com.lckback.lckforall.aboutlck.dto.player.FindPlayerTeamHistoryDto;
import com.lckback.lckforall.aboutlck.dto.player.FindPlayerWinningHistoryDto;
import com.lckback.lckforall.aboutlck.service.AboutLckPlayerService;
import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.base.setting.SwaggerPageable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/aboutlck/player")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT Token")
@Tag(name = "AboutLckPlayer", description = "AboutLck 선수 정보 조회 API")
public class AboutLckPlayerController {

	private final AboutLckPlayerService aboutLckPlayerService;

	@Operation(summary = "선수 정보 조회 API", description = "특정 선수의 정보를 조회합니다.")
	@GetMapping("/{playerId}/information")
	public ApiResponse<FindPlayerInformationDto.Response> findPlayerInformation(
		@PathVariable("playerId") Long playerId) {
		FindPlayerInformationDto.Parameter parameter = FindPlayerInformationDto.Parameter.builder()
			.playerId(playerId)
			.build();

		FindPlayerInformationDto.Response data = aboutLckPlayerService.findPlayerInformation(parameter);

		return ApiResponse.createSuccess(data);
	}

	@SwaggerPageable
	@Operation(summary = "선수 팀 이력 조회 API", description = "특정 선수의 팀 이력을 조회합니다.")
	@GetMapping("/{playerId}/team-history")
	public ApiResponse<FindPlayerTeamHistoryDto.Response> findPlayerTeamHistory(
		@PathVariable("playerId") Long playerId,
		Pageable pageable) {
		FindPlayerTeamHistoryDto.Parameter parameter = FindPlayerTeamHistoryDto.Parameter.builder()
			.playerId(playerId)
			.pageable(pageable)
			.build();

		FindPlayerTeamHistoryDto.Response data = aboutLckPlayerService.findPlayerTeamHistory(parameter);
		return ApiResponse.createSuccess(data);
	}

	@SwaggerPageable
	@Operation(summary = "선수 우승 기록 조회 API", description = "특정 선수의 우승 기록을 조회합니다.")
	@GetMapping("/{playerId}/winning-history")
	public ApiResponse<FindPlayerWinningHistoryDto.Response> findPlayerWinningHistory(
		@PathVariable("playerId") Long playerId,
		Pageable pageable) {
		FindPlayerWinningHistoryDto.Parameter parameter = FindPlayerWinningHistoryDto.Parameter.builder()
			.playerId(playerId)
			.pageable(pageable)
			.build();

		FindPlayerWinningHistoryDto.Response data = aboutLckPlayerService.findPlayerWinningHistory(
			parameter);
		return ApiResponse.createSuccess(data);
	}
}
