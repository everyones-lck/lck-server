package com.lckback.lckforall.aboutlck.dto.team;

import java.util.List;

import org.springframework.data.domain.Pageable;

import lombok.Builder;
import lombok.Getter;

public class FindTeamWinningHistoryDto {
	@Getter
	@Builder
	public static class Parameter {
		private Long teamId;
		private Pageable pageable;
	}

	@Getter
	@Builder
	public static class Response {
		private List<String> seasonNameList;
		private Integer totalPage;
		private Long totalElements;
		private Boolean isFirst;
		private Boolean isLast;
	}
}
