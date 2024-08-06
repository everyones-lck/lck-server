package com.lckback.lckforall.match.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PogInfoDto {
	@Getter
	@AllArgsConstructor
	public static class PogResponse {
		private Long id;

		private String name;

		private String profileImageUrl;

		private String seasonInfo;

		private Integer matchNumber;

		private LocalDateTime matchDate;

		public static PogResponse create(Long pogId, String name, String profileImageUrl, String seasonInfo,
			Integer matchNumber, LocalDateTime matchDate) {
			return new PogResponse(pogId, name, profileImageUrl, seasonInfo, matchNumber, matchDate);
		}
	}

	@Getter
	@AllArgsConstructor
	public static class PogServiceDto {
		private Long matchId;

		public static PogServiceDto create(Long matchId) {
			return new PogServiceDto(matchId);
		}
	}

	@Getter
	@NoArgsConstructor
	public static class MatchPogRequest {
		private Long matchId;

		public PogServiceDto toDto() {
			return PogServiceDto.create(matchId);
		}

	}

}
