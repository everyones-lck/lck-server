package com.lckback.lckforall.base.api.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SeasonErrorCode implements ErrorCode {
	NOT_EXIST_SEASON(HttpStatus.NOT_FOUND, "해당 시즌은 존재하지 않습니다");

	private final HttpStatus httpStatus;
	private final String message;
}
