package com.lckback.lckforall.aboutlck.dto.team;

import java.util.List;

import org.springframework.data.domain.Pageable;

import lombok.Builder;
import lombok.Getter;

public class FindTeamRatingHistoryDto {
	@Getter
	@Builder
	public static class Parameter {
		private Long teamId;
		private Pageable pageable;
	}

	@Getter
	@Builder
	public static class Response {
		private List<SeasonDetail> seasonDetailList;
		private Integer totalPage;
		private Long totalElements;
		private Boolean isFirst;
		private Boolean isLast;
	}

	@Getter
	@Builder
	public static class SeasonDetail {
		private String seasonName;
		private Integer rating;
	}
}
