package com.lckback.lckforall.base.api.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MatchErrorCode implements ErrorCode {
	NOT_EXIST_MATCH(HttpStatus.NOT_FOUND, "해당 경기는 존재하지 않습니다"),

	ALREADY_VOTE(HttpStatus.METHOD_NOT_ALLOWED, "이미 투표한 경기입니다");

	private final HttpStatus httpStatus;
	private final String message;
}
