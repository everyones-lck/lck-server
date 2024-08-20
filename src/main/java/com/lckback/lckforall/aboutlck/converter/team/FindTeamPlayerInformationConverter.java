package com.lckback.lckforall.aboutlck.converter.team;

import java.util.List;

import com.lckback.lckforall.aboutlck.dto.team.FindTeamPlayerInformationDto;
import com.lckback.lckforall.player.model.Player;
import com.lckback.lckforall.player.model.SeasonTeamPlayer;

public class FindTeamPlayerInformationConverter {

	public static FindTeamPlayerInformationDto.Response convertToResponse(List<SeasonTeamPlayer> seasonTeamPlayers) {
		List<FindTeamPlayerInformationDto.PlayerDetail> playerDetails = seasonTeamPlayers.stream()
			.map(seasonTeamPlayer ->
				convertToPlayerDetail(seasonTeamPlayer.getPlayer(), seasonTeamPlayer.getIsCaptain()))
			.toList();

		return FindTeamPlayerInformationDto.Response.builder()
			.playerDetails(playerDetails)
			.numberOfPlayerDetails(playerDetails.size())
			.build();
	}

	private static FindTeamPlayerInformationDto.PlayerDetail convertToPlayerDetail(Player player, Boolean isCaptain) {
		return FindTeamPlayerInformationDto.PlayerDetail.builder()
			.playerId(player.getId())
			.playerName(player.getName())
			.playerRole(player.getRole())
			.position(player.getPosition())
			.profileImageUrl(player.getProfileImageUrl())
			.isCaptain(isCaptain)
			.build();
	}
}
