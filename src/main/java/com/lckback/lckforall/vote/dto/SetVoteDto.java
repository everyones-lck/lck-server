package com.lckback.lckforall.vote.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class SetVoteDto {

	@Getter
	@AllArgsConstructor
	public static class SetPogVoteCandidateResponse {
		private List<SetVoteDto.PlayerInformation> information;
	}

	@Getter
	@AllArgsConstructor
	public static class VoteCandidateDto {
		private Long userId;

		private Long matchId;

		private Integer setIndex;

	}
	@Getter
	@AllArgsConstructor
	public static class SetPogVoteDto {
		private Long userId;

		private Long matchId;

		private Integer setIndex;

		private Long playerId;

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
	public static class SetPogVoteRequest {
		private Long matchId;

		private Integer setIndex;

		private Long playerId;

		public SetVoteDto.SetPogVoteDto toDto(Long userId) {
			return new SetVoteDto.SetPogVoteDto(userId, matchId, setIndex, playerId);
		}
	}
}
