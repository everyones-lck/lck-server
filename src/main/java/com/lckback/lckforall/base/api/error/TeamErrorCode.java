package com.lckback.lckforall.base.api.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TeamErrorCode implements ErrorCode {

	NOT_EXISTS_TEAM(HttpStatus.BAD_REQUEST, "존재하지 않는 팀입니다."),
	MY_TEAM_CANNOT_UPDATE(HttpStatus.BAD_REQUEST, "마이 팀은 한 달에 한 번 변경 가능합니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
