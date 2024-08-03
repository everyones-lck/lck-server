package com.lckback.lckforall.base.api.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TeamErrorCode implements ErrorCode {
	NOT_EXIST_TEAM(HttpStatus.NOT_FOUND, "해당 팀은 존재하지 않습니다");

	private final HttpStatus httpStatus;
	private final String message;
}
