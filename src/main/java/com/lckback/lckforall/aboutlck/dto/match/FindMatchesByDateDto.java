package com.lckback.lckforall.aboutlck.dto.match;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

public class FindMatchesByDateDto {

	@Getter
	@Builder
	public static class Response {
		private List<MatchDetail> matchDetailList;
		private Integer listSize;
	}

	@Getter
	@Builder
	public static class Parameter {
		private LocalDate searchDate;
	}

	@Getter
	@Builder
	public static class MatchDetail {
		private TeamDetail team1;
		private TeamDetail team2;
		private boolean matchFinished;
		private String season;
		private Integer matchNumber;
		private LocalDate matchDate;
	}

	@Getter
	@Builder
	public static class TeamDetail {
		private String teamName;
		private String teamLogoUrl;
		private boolean isWinner;
	}
}
