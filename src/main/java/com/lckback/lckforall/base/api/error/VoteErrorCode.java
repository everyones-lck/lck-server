package com.lckback.lckforall.base.api.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VoteErrorCode implements ErrorCode {
	ALREADY_VOTE(HttpStatus.METHOD_NOT_ALLOWED, "이미 투표한 경기입니다"),
	NOT_VOTE_TIME(HttpStatus.METHOD_NOT_ALLOWED, "투표 기간이 아닙니다"),
	NOT_EXIST_VOTE(HttpStatus.NOT_FOUND, "투표를 찾을수 없습니다."),
	ONLY_VOTE_FOR_WINNER(HttpStatus.METHOD_NOT_ALLOWED, "이긴 팀의 플레이어에게만 투표할 수 있습니다"),
	VOTE_NOT_FINISHED_YET(HttpStatus.METHOD_NOT_ALLOWED,"투표가 아직 종료되지 않았습니다");

	private final HttpStatus httpStatus;
	private final String message;
}
