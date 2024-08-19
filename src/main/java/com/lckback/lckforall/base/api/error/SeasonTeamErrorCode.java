package com.lckback.lckforall.base.api.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SeasonTeamErrorCode implements ErrorCode {

	NOT_EXIST_SEASON_TEAM(HttpStatus.NOT_FOUND, "시즌에 해당 팀이 존재하지 않습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
