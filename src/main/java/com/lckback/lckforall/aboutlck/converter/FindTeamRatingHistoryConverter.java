package com.lckback.lckforall.aboutlck.converter;

import org.springframework.data.domain.Page;

import com.lckback.lckforall.aboutlck.dto.FindTeamRatingHistoryDto;
import com.lckback.lckforall.team.model.SeasonTeam;

public class FindTeamRatingHistoryConverter {
	public static FindTeamRatingHistoryDto.Response convertToResponse(Page<SeasonTeam> seasonTeams) {
		return FindTeamRatingHistoryDto.Response.builder()
			.seasonDetailList(seasonTeams.map(FindTeamRatingHistoryConverter::convertToSeasonDetail).toList())
			.totalElements(seasonTeams.getTotalElements())
			.totalPage(seasonTeams.getTotalPages())
			.isFirst(seasonTeams.isFirst())
			.isLast(seasonTeams.isLast())
			.build();
	}

	public static FindTeamRatingHistoryDto.SeasonDetail convertToSeasonDetail(SeasonTeam seasonTeam) {
		return FindTeamRatingHistoryDto.SeasonDetail.builder()
			.seasonName(seasonTeam.getSeason().getName())
			.rating(seasonTeam.getRating())
			.build();
	}
}
