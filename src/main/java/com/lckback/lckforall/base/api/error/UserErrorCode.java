package com.lckback.lckforall.base.api.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
	NOT_EXIST_USER(HttpStatus.NOT_FOUND, "해당 유저는 존재하지 않습니다");

	private final HttpStatus httpStatus;
	private final String message;
}
