package com.lckback.lckforall.base.api.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

	USER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없는 사용자입니다."),
	USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "사용자가 이미 존재합니다."),
	INVALID_KAKAO_USER_ID(HttpStatus.UNAUTHORIZED, "유효하지 않은 카카오 유저 아이디입니다."),
	INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Access 토큰입니다."),
	INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh 토큰입니다."),
    ALREADY_EXIST_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    NOT_EXIST_USER(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
