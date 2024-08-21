package com.lckback.lckforall.aboutlck.converter.team;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.lckback.lckforall.aboutlck.dto.team.FindTeamPlayerHistoryDto;
import com.lckback.lckforall.base.type.PlayerRole;
import com.lckback.lckforall.player.model.Player;
import com.lckback.lckforall.team.model.SeasonTeam;

public class FindTeamPlayerHistoryConverter {
	public static FindTeamPlayerHistoryDto.Response convertToResponse(Page<SeasonTeam> seasonTeams) {
		// season의 년도별로 정렬해서 첫번째 seasonTeam의 list를 가져옴
		List<SeasonTeam> list = seasonTeams.stream()
			.collect(Collectors.groupingBy(seasonTeam -> seasonTeam.getSeason().getName().substring(0, 4)))
			.values()
			.stream()
			.filter(seasonTeamList -> !seasonTeamList.isEmpty())
			.map(seasonTeamList -> seasonTeamList.get(0))
			.toList();

		return FindTeamPlayerHistoryDto.Response.builder()
			.seasonDetails(list
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
			.filter(seasonTeamPlayer -> seasonTeamPlayer.getPlayer().getRole().equals(PlayerRole.LCK_ROSTER))
			.map(seasonTeamPlayer -> convertToPlayerDetail(seasonTeamPlayer.getPlayer()))
			.toList();

		// seasonTeam의 년도만 seasonName에 넣음
		return FindTeamPlayerHistoryDto.SeasonDetail.builder()
			.players(playerDetailList)
			.numberOfPlayerDetail(playerDetailList.size())
			.seasonName(seasonTeam.getSeason().getName().substring(0,4))
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
