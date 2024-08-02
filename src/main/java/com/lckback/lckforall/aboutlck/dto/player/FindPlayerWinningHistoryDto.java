package com.lckback.lckforall.aboutlck.dto.player;


import java.util.List;

import org.springframework.data.domain.Pageable;

import lombok.Builder;
import lombok.Getter;

public class FindPlayerWinningHistoryDto {
	@Getter
	@Builder
	public static class Parameter {
		private Pageable pageable;
		private Long playerId;
	}

	@Getter
	@Builder
	public static class Response {
		private List<String> seasonNames;
		private Integer totalPage;
		private Long totalElements;
		private Boolean isFirst;
		private Boolean isLast;
	}
}
