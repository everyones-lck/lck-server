package com.lckback.lckforall.aboutlck.converter;

import org.springframework.data.domain.Page;

import com.lckback.lckforall.aboutlck.dto.FindTeamWinningHistoryDto;
import com.lckback.lckforall.team.model.SeasonTeam;

public class FindTeamWinningHistoryConverter {

	public static FindTeamWinningHistoryDto.Response convertToResponse(Page<SeasonTeam> seasonTeams) {
		return FindTeamWinningHistoryDto.Response.builder()
			.seasonNameList(seasonTeams.stream().map(seasonTeam -> seasonTeam.getSeason().getName()).toList())
			.totalElements(seasonTeams.getTotalElements())
			.totalPage(seasonTeams.getTotalPages())
			.isLast(seasonTeams.isLast())
			.isFirst(seasonTeams.isFirst())
			.build();
	}
}
