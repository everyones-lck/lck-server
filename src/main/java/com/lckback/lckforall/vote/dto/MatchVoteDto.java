package com.lckback.lckforall.vote.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class MatchVoteDto {
	@Getter
	@AllArgsConstructor
	public static class VoteCandidateResponse {
		private String seasonName;

		private Integer matchNumber;

		private Long team1Id;

		private String team1Logo;

		private Long team2Id;

		private String team2Logo;
	}

	@Getter
	@AllArgsConstructor
	public static class VoteCandidateDto {
		private Long userId;

		private Long matchId;

	}

	@Getter
	@AllArgsConstructor
	public static class MakeVoteDto {
		private Long userId;

		private Long matchId;

		private Long teamId;

	}

	@Getter
	@AllArgsConstructor
	public static class MakeVoteRequest {

		private Long matchId;

		private Long teamId;

		public MatchVoteDto.MakeVoteDto toDto(Long userId) {
			return new MakeVoteDto(userId, matchId, teamId);
		}
	}

}
