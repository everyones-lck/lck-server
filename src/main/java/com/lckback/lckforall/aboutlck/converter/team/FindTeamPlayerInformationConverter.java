package com.lckback.lckforall.aboutlck.converter.team;

import java.util.List;

import com.lckback.lckforall.aboutlck.dto.team.FindTeamPlayerInformationDto;
import com.lckback.lckforall.player.model.Player;

public class FindTeamPlayerInformationConverter {

	public static FindTeamPlayerInformationDto.Response convertToResponse(List<Player> players) {
		List<FindTeamPlayerInformationDto.PlayerDetail> playerDetails = players.stream()
			.map(FindTeamPlayerInformationConverter::convertToPlayerDetail)
			.toList();

		return FindTeamPlayerInformationDto.Response.builder()
			.playerDetails(playerDetails)
			.numberOfPlayerDetails(playerDetails.size())
			.build();
	}

	private static FindTeamPlayerInformationDto.PlayerDetail convertToPlayerDetail(Player player) {
		return FindTeamPlayerInformationDto.PlayerDetail.builder()
			.playerId(player.getId())
			.playerName(player.getName())
			.playerRole(player.getRole())
			.profileImageUrl(player.getProfileImageUrl())
			.build();
	}
}
