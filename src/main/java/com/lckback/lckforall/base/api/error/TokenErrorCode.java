package com.lckback.lckforall.base.api.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenErrorCode implements ErrorCode {

	INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Access 토큰입니다."),
	INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh 토큰입니다."),
	EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 Access 토큰입니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
