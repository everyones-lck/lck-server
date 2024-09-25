package com.lckback.lckforall.vote.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class MatchVoteDto {
	@Getter
	@AllArgsConstructor
	public static class MatchPredictionCandidateResponse {
		private String seasonName;

		private Integer matchNumber;

		private Long team1Id;

		private String team1Logo;

		private Long team2Id;

		private String team2Logo;

		private Long myVoteTeamId;
	}

	@Getter
	@AllArgsConstructor
	public static class MatchPredictionCandidateDto {
		private String kakaoUserId;

		private Long matchId;

	}

	@Getter
	@AllArgsConstructor
	public static class MatchPredictionDto {
		private String kakaoUserId;

		private Long matchId;

		private Long teamId;

	}

	@Getter
	@AllArgsConstructor
	public static class MatchPredictionRequest {

		private Long matchId;

		private Long teamId;

		public MatchPredictionDto toDto(String kakaoUserId) {
			return new MatchPredictionDto(kakaoUserId, matchId, teamId);
		}
	}

	@Getter
	@AllArgsConstructor
	public static class MatchPogVoteDto {
		private String kakaoUserId;

		private Long matchId;

		private Long playerId;

	}

	@Getter
	@AllArgsConstructor
	public static class MatchPogVoteRequest {

		private Long matchId;

		private Long playerId;

		public  MatchPogVoteDto toDto(String kakaoUserId) {
			return new MatchPogVoteDto(kakaoUserId, matchId, playerId);
		}
	}


	@Getter
	@AllArgsConstructor
	public static class PlayerInformation {
		private Long playerId;

		private String playerProfileImageUrl;

		private String playerName;
	}

	@Getter
	@AllArgsConstructor
	public static class MatchPogVoteCandidate {
		private List<PlayerInformation> information;
	}
}
