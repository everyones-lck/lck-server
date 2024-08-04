package com.lckback.lckforall.vote.dto;

import java.util.List;

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
	public static class MatchPredictDto {
		private Long userId;

		private Long matchId;

		private Long teamId;

	}

	@Getter
	@AllArgsConstructor
	public static class MatchPredictVoteRequest {

		private Long matchId;

		private Long teamId;

		public MatchPredictDto toDto(Long userId) {
			return new MatchPredictDto(userId, matchId, teamId);
		}
	}

	@Getter
	@AllArgsConstructor
	public static class MatchPogVoteDto {
		private Long userId;

		private Long matchId;

		private Long playerId;

	}

	@Getter
	@AllArgsConstructor
	public static class MatchPogVoteRequest {

		private Long matchId;

		private Long playerId;

		public  MatchPogVoteDto toDto(Long userId) {
			return new MatchPogVoteDto(userId, matchId, playerId);
		}
	}


	@Getter
	@AllArgsConstructor
	public static class PlayerInformation {
		private Long playerId;

		private String playerImgUrl;

		private String playerName;
	}

	@Getter
	@AllArgsConstructor
	public static class MatchPogVoteCandidateResponse {
		private List<PlayerInformation> information;
	}
}
