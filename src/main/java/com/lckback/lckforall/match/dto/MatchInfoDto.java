package com.lckback.lckforall.match.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class MatchInfoDto {
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TodayMatchResponse { // today's match response
		private Long id;

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

}
