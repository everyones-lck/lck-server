package com.lckback.lckforall.match.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.lckback.lckforall.match.model.Match;
import com.lckback.lckforall.player.model.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PogInfoDto {

	@Getter
	@AllArgsConstructor
	public static class PogResponse{
		private String seasonInfo;

		private Integer matchNumber;

		private LocalDateTime matchDate;

		private List<SetPogDTO> setPogResponses;

		private MatchPogDTO matchPogResponse;

		public PogResponse(Match match){
			seasonInfo = match.getSeason().getName();
			matchNumber = match.getMatchNumber();
			matchDate = match.getMatchDate();
			setPogResponses = new ArrayList<SetPogDTO>();
		}
		public void addSetPog(Player player, Integer setIndex){
			setPogResponses.add(new SetPogDTO(setIndex, player.getId(),player.getName(), player.getProfileImageUrl()));
		}
		public void setMatchPog(Player player){
			matchPogResponse = new MatchPogDTO(player.getId(),player.getName(), player.getProfileImageUrl());
		}
	}
	@Getter
	@AllArgsConstructor
	public static class SetPogDTO {
		private Integer SetIndex;

		private Long PlayerId;

		private String name;

		private String profileImageUrl;

	}
	@Getter
	@AllArgsConstructor
	public static class MatchPogDTO {

		private Long PlayerId;

		private String name;

		private String profileImageUrl;

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
