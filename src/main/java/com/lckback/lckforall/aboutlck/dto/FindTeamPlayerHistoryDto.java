package com.lckback.lckforall.aboutlck.dto;


import java.util.List;

import org.springframework.data.domain.Pageable;

import com.lckback.lckforall.base.type.PlayerPosition;
import com.lckback.lckforall.base.type.PlayerRole;

import lombok.Builder;
import lombok.Getter;

public class FindTeamPlayerHistoryDto {
	@Getter
	@Builder
	public static class Parameter {
		private Pageable pageable;
		private Long teamId;
	}

	@Getter
	@Builder
	public static class Response {
		private List<SeasonDetail> seasonDetails;
		private Integer totalPage;
		private Long totalElements;
		private Boolean isFirst;
		private Boolean isLast;
	}

	@Getter
	@Builder
	public static class SeasonDetail {
		private List<PlayerDetail> players;
		private Integer playerDetailSize;
		private String seasonName;
	}

	@Getter
	@Builder
	public static class PlayerDetail {
		private Long playerId;
		private String playerName;
		private PlayerRole playerRole;
		private PlayerPosition playerPosition;
	}
}
