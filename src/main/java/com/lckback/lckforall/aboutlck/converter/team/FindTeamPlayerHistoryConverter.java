package com.lckback.lckforall.aboutlck.converter.team;

import java.util.List;

import org.springframework.data.domain.Page;

import com.lckback.lckforall.aboutlck.dto.team.FindTeamPlayerHistoryDto;
import com.lckback.lckforall.player.model.Player;
import com.lckback.lckforall.team.model.SeasonTeam;

public class FindTeamPlayerHistoryConverter {
	public static FindTeamPlayerHistoryDto.Response convertToResponse(Page<SeasonTeam> seasonTeams) {
		return FindTeamPlayerHistoryDto.Response.builder()
			.seasonDetails(seasonTeams
				.stream()
				.map(FindTeamPlayerHistoryConverter::convertToSeasonDetail)
				.toList())
			.totalPage(seasonTeams.getTotalPages())
			.totalElements(seasonTeams.getTotalElements())
			.isFirst(seasonTeams.isFirst())
			.isLast(seasonTeams.isLast())
			.build();
	}

	private static FindTeamPlayerHistoryDto.SeasonDetail convertToSeasonDetail(SeasonTeam seasonTeam) {
		List<FindTeamPlayerHistoryDto.PlayerDetail> playerDetailList = seasonTeam
			.getSeasonTeamPlayers()
			.stream()
			.map(seasonTeamPlayer -> convertToPlayerDetail(seasonTeamPlayer.getPlayer()))
			.toList();

		return FindTeamPlayerHistoryDto.SeasonDetail.builder()
			.players(playerDetailList)
			.numberOfPlayerDetail(playerDetailList.size())
			.seasonName(seasonTeam.getSeason().getName())
			.build();
	}

	private static FindTeamPlayerHistoryDto.PlayerDetail convertToPlayerDetail(Player player) {
		return FindTeamPlayerHistoryDto.PlayerDetail.builder()
			.playerId(player.getId())
			.playerName(player.getName())
			.playerRole(player.getRole())
			.playerPosition(player.getPosition())
			.build();
	}
}
