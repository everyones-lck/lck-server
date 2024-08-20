package com.lckback.lckforall.match.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import com.lckback.lckforall.base.type.MatchResult;

public class MatchInfoDto {

	@Getter
	@Builder
	public static class Response {
		private List<TodayMatchResponse> matchResponses;
		private Integer matchResponseSize;
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TodayMatchResponse { // today's match response
		private Long matchId;

		private LocalDateTime matchDate;

		private String team1Name;

		private String team1LogoUrl;

		private String team2Name;

		private String team2LogoUrl;

		private Double team1VoteRate; // 승부 예측 결과 1팀의 득표 비율

		private Double team2VoteRate;

		private String seasonInfo; // ex) 2024 spring

		private Integer matchNumber; // ex) 1st match, 2nd match
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TodayMatchDto { // today's match response
		private Long matchId;

		private LocalDateTime matchDate;

		private String team1Name;

		private String team1LogoUrl;

		private String team2Name;

		private String team2LogoUrl;

		private String seasonInfo; // ex) 2024 spring

		private Integer matchNumber; // ex) 1st match, 2nd match
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class RecentMatchResultDto {
		private Long matchId;

		private LocalDateTime matchDate;

		private String team1Name;

		private String team1LogoUrl;

		private String team2Name;

		private String team2LogoUrl;

		private MatchResult matchResult;

	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class HomePageResponse { // today's match response
		List<TodayMatchDto> todayMatches;
		List<RecentMatchResultDto> recentMatchResults;
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class setCountResponse {
		private Integer setCount;
	}
}
