package com.lckback.lckforall.aboutlck.dto;

import java.util.List;

import org.springframework.data.domain.Pageable;

import lombok.Builder;
import lombok.Getter;

public class FindTeamRatingBySeasonDto {

	@Getter
	@Builder
	public static class Parameter {
		String seasonName;
		Pageable pageable;
	}

	@Getter
	@Builder
	public static class Response {
		private List<TeamDetail> teamDetailList;
		private Integer totalPage;
		private Long totalElements;
		private Boolean isFirst;
		private Boolean isLast;
	}

	@Getter
	@Builder
	public static class TeamDetail {
		private Long teamId;
		private String teamName;
		private String teamLogoUrl;
		private Integer rating;
	}
}
