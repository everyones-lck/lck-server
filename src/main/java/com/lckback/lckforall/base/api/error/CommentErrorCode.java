package com.lckback.lckforall.base.api.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements ErrorCode {

	COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글이 없습니다."),
	COMMENT_ALREADY_REPORTED(HttpStatus.BAD_REQUEST, "이미 신고한 댓글입니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
