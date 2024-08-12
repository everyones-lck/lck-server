package com.lckback.lckforall.aboutlck.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lckback.lckforall.aboutlck.dto.team.FindTeamPlayerHistoryDto;
import com.lckback.lckforall.aboutlck.dto.team.FindTeamPlayerInformationDto;
import com.lckback.lckforall.aboutlck.dto.team.FindTeamRatingHistoryDto;
import com.lckback.lckforall.aboutlck.dto.team.FindTeamRatingBySeasonDto;
import com.lckback.lckforall.aboutlck.dto.team.FindTeamWinningHistoryDto;
import com.lckback.lckforall.aboutlck.service.AboutLckTeamService;
import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.base.setting.SwaggerPageable;
import com.lckback.lckforall.base.type.PlayerRole;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/aboutlck/team")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT Token")
@Tag(name = "AboutLckTeam", description = "AboutLck 팀 정보 조회 API")
public class AboutLckTeamController {

	private final AboutLckTeamService aboutLckTeamService;

	@SwaggerPageable
	@Operation(summary = "팀 순위 조회 API", description = "특정 시즌의 팀 순위를 조회합니다.")
	@GetMapping("/rating")
	public ApiResponse<FindTeamRatingBySeasonDto.Response> findTeamRatingBySeason(
		@RequestParam("seasonName") String seasonName,
		Pageable pageable) {
		FindTeamRatingBySeasonDto.Parameter parameter = FindTeamRatingBySeasonDto.Parameter.builder()
			.seasonName(seasonName)
			.pageable(pageable)
			.build();

		FindTeamRatingBySeasonDto.Response data = aboutLckTeamService.findTeamRatingBySeason(parameter);
		return ApiResponse.createSuccess(data);
	}

	@SwaggerPageable
	@Operation(summary = "팀 우승 기록 조회 API", description = "특정 팀의 우승 기록을 조회합니다.")
	@GetMapping("/{teamId}/winning-history")
	public ApiResponse<FindTeamWinningHistoryDto.Response> findTeamWinningHistory(
		@PathVariable("teamId") Long teamId,
		Pageable pageable) {
		FindTeamWinningHistoryDto.Parameter parameter = FindTeamWinningHistoryDto.Parameter.builder()
			.teamId(teamId)
			.pageable(pageable)
			.build();

		FindTeamWinningHistoryDto.Response data = aboutLckTeamService.findTeamWinningHistory(parameter);
		return ApiResponse.createSuccess(data);
	}

	@SwaggerPageable
	@Operation(summary = "팀 최근 시즌 순위 조회 API", description = "특정 팀의 최근 순위를 조회합니다.")
	@GetMapping("/{teamId}/rating-history")
	public ApiResponse<FindTeamRatingHistoryDto.Response> findTeamRatingHistory(
		@PathVariable("teamId") Long teamId,
		Pageable pageable) {
		FindTeamRatingHistoryDto.Parameter parameter = FindTeamRatingHistoryDto.Parameter.builder()
			.teamId(teamId)
			.pageable(pageable)
			.build();

		FindTeamRatingHistoryDto.Response data = aboutLckTeamService.findTeamRatingHistory(parameter);
		return ApiResponse.createSuccess(data);
	}

	@SwaggerPageable
	@Operation(summary = "팀 선수 기록 조회 API", description = "특정 팀의 선수 기록을 조회합니다.")
	@GetMapping("/{teamId}/player-history")
	public ApiResponse<FindTeamPlayerHistoryDto.Response> findTeamPlayerHistory(
		@PathVariable("teamId") Long teamId,
		Pageable pageable) {
		FindTeamPlayerHistoryDto.Parameter parameter = FindTeamPlayerHistoryDto.Parameter.builder()
			.teamId(teamId)
			.pageable(pageable)
			.build();

		FindTeamPlayerHistoryDto.Response data = aboutLckTeamService.findTeamPlayerHistory(parameter);
		return ApiResponse.createSuccess(data);
	}

	@Operation(summary = "팀 선수 정보 조회 API", description = "특정 팀의 선수 정보를 조회합니다.")
	@GetMapping("/{teamId}/player-information")
	public ApiResponse<FindTeamPlayerInformationDto.Response> findTeamPlayerInformationBySeasonAndRole(
		@PathVariable("teamId") Long teamId,
		@RequestParam("seasonName") String seasonName,
		@RequestParam("player_role") PlayerRole role) {
		FindTeamPlayerInformationDto.Parameter parameter = FindTeamPlayerInformationDto.Parameter.builder()
			.teamId(teamId)
			.seasonName(seasonName)
			.playerRole(role)
			.build();

		FindTeamPlayerInformationDto.Response data = aboutLckTeamService.findTeamPlayerInformation(
			parameter);
		return ApiResponse.createSuccess(data);
	}
}
