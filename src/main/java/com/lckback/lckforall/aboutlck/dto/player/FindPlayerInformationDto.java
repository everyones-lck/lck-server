package com.lckback.lckforall.aboutlck.dto.player;

import java.time.LocalDate;

import com.lckback.lckforall.base.type.PlayerPosition;

import lombok.Builder;
import lombok.Getter;

public class FindPlayerInformationDto {
	@Getter
	@Builder
	public static class Parameter {
		private Long playerId;
	}

	@Getter
	@Builder
	public static class Response {
		private String nickName;
		private String realName;
		private String playerProfileImageUrl;
		private LocalDate birthDate;
		private PlayerPosition position;
	}
}
