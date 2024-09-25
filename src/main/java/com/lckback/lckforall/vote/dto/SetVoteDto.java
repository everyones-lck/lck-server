package com.lckback.lckforall.vote.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class SetVoteDto {

	@Getter
	@AllArgsConstructor
	public static class SetPogVoteCandidate {
		private Integer setIndex;
		private List<SetVoteDto.PlayerInformation> candidates;
	}

	@Getter
	@AllArgsConstructor
	public static class SetPogVoteCandidateResponse {
		private List<SetPogVoteCandidate> information;

		public void addCandidates(SetPogVoteCandidate candidates){
			information.add(candidates);
		}
		public SetPogVoteCandidateResponse() {
			information = new ArrayList<SetPogVoteCandidate>();
		}
	}

	@Getter
	@AllArgsConstructor
	public static class VoteCandidateDto {
		private String kakaoUserId;

		private Long matchId;

	}
	@Getter
	@AllArgsConstructor
	public static class SetPogVoteDto {
		private String kakaoUserId;

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

		public SetVoteDto.SetPogVoteDto toDto(String kakaoId) {
			return new SetVoteDto.SetPogVoteDto(kakaoId, matchId, setIndex, playerId);
		}
	}
}
