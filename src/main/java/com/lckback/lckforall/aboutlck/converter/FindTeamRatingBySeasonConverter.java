package com.lckback.lckforall.aboutlck.converter;

import java.util.List;

import org.springframework.data.domain.Page;

import com.lckback.lckforall.aboutlck.dto.FindTeamRatingBySeasonDto;
import com.lckback.lckforall.team.model.SeasonTeam;

public class FindTeamRatingBySeasonConverter {

	public static FindTeamRatingBySeasonDto.Response convertToResponse(Page<SeasonTeam> seasonTeams) {
		List<FindTeamRatingBySeasonDto.TeamDetail> teamDetailList = seasonTeams.stream()
			.map(FindTeamRatingBySeasonConverter::convertToTeamDetailList)
			.toList();

		return FindTeamRatingBySeasonDto.Response.builder()
			.teamDetailList(teamDetailList)
			.totalPage(seasonTeams.getTotalPages())
			.totalElements(seasonTeams.getTotalElements())
			.isFirst(seasonTeams.isFirst())
			.isLast(seasonTeams.isLast())
			.build();
	}

	private static FindTeamRatingBySeasonDto.TeamDetail convertToTeamDetailList(SeasonTeam seasonTeam) {
		return FindTeamRatingBySeasonDto.TeamDetail.builder()
			.teamId(seasonTeam.getTeam().getId())
			.teamName(seasonTeam.getTeam().getTeamName())
			.teamLogoUrl(seasonTeam.getTeam().getTeamLogoUrl())
			.rating(seasonTeam.getRating())
			.build();
	}
}
