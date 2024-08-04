package com.lckback.lckforall.base.api.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VoteErrorCode implements ErrorCode {
	ALREADY_VOTE(HttpStatus.METHOD_NOT_ALLOWED, "이미 투표한 경기입니다"),
	VOTE_TIME_OVER(HttpStatus.METHOD_NOT_ALLOWED, "투표 기간이 아닙니다"),
	MATCH_NOT_END(HttpStatus.METHOD_NOT_ALLOWED, "아직 종료되지 않은 경기입니다");

	private final HttpStatus httpStatus;
	private final String message;
}
