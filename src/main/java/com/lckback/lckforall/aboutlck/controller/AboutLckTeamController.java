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
import com.lckback.lckforall.base.type.PlayerRole;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/aboutlck/team")
@RequiredArgsConstructor
public class AboutLckTeamController {

	private final AboutLckTeamService aboutLckTeamService;

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
