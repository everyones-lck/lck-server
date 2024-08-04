package com.lckback.lckforall.aboutlck.converter.player;

import org.springframework.data.domain.Page;

import com.lckback.lckforall.aboutlck.dto.player.FindPlayerWinningHistoryDto;
import com.lckback.lckforall.team.model.SeasonTeam;

public class FindPlayerWinningHistoryConverter {
	public static FindPlayerWinningHistoryDto.Response toResponse(Page<SeasonTeam> seasonTeams) {
		return FindPlayerWinningHistoryDto.Response.builder()
			.seasonNames(seasonTeams.stream().map(seasonTeam -> seasonTeam.getSeason().getName()).toList())
			.totalPage(seasonTeams.getTotalPages())
			.totalElements(seasonTeams.getTotalElements())
			.isFirst(seasonTeams.isFirst())
			.isLast(seasonTeams.isLast())
			.build();
	}
}
