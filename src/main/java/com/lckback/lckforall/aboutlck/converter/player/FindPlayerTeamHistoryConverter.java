package com.lckback.lckforall.aboutlck.converter.player;

import java.util.List;

import org.springframework.data.domain.Page;

import com.lckback.lckforall.aboutlck.dto.player.FindPlayerTeamHistoryDto;
import com.lckback.lckforall.team.model.SeasonTeam;

public class FindPlayerTeamHistoryConverter {
	public static FindPlayerTeamHistoryDto.Response toResponse(Page<SeasonTeam> seasonTeams) {
		List<FindPlayerTeamHistoryDto.SeasonTeamDetail> seasonTeamDetails = seasonTeams.stream()
			.map(FindPlayerTeamHistoryConverter::toSeasonTeamDetail)
			.toList();

		return FindPlayerTeamHistoryDto.Response.builder()
			.seasonTeamDetails(seasonTeamDetails)
			.totalPage(seasonTeams.getTotalPages())
			.totalElements(seasonTeams.getTotalElements())
			.isFirst(seasonTeams.isFirst())
			.isLast(seasonTeams.isLast())
			.build();
	}

	private static FindPlayerTeamHistoryDto.SeasonTeamDetail toSeasonTeamDetail(SeasonTeam seasonTeam) {
		return FindPlayerTeamHistoryDto.SeasonTeamDetail.builder()
			.teamName(seasonTeam.getTeam().getTeamName())
			.seasonName(seasonTeam.getSeason().getName())
			.build();
	}
}
