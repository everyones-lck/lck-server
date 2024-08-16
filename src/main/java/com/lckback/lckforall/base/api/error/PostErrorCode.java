package com.lckback.lckforall.base.api.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시물이 없습니다."),
	POST_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "포스트 타입을 찾을 수 없습니다."),
	POST_FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "포스트 파일을 찾을 수 없습니다.");
	private final HttpStatus httpStatus;
	private final String message;
}
