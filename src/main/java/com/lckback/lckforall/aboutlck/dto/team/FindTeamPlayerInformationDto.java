package com.lckback.lckforall.aboutlck.dto.team;

import java.util.List;

import com.lckback.lckforall.base.type.PlayerRole;

import lombok.Builder;
import lombok.Getter;

public class FindTeamPlayerInformationDto {
	@Getter
	@Builder
	public static class Parameter {
		private Long teamId;
		private String seasonName;
		private PlayerRole playerRole;
	}

	@Getter
	@Builder
	public static class Response {
		private List<PlayerDetail> playerDetails;
		private Integer numberOfPlayerDetails;
	}

	@Getter
	@Builder
	public static class PlayerDetail {
		private Long playerId;
		private String playerName;
		private PlayerRole playerRole;
		private String profileImageUrl;
	}
}
