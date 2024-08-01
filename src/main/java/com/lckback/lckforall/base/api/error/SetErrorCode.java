package com.lckback.lckforall.base.api.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SetErrorCode implements ErrorCode {
	WRONG_SET(HttpStatus.NOT_FOUND, "잘못된 세트 접근입니다");

	private final HttpStatus httpStatus;
	private final String message;
}
