package com.lckback.lckforall.base.api.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements ErrorCode {
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글이 없습니다."),
    COMMENT_NOT_VALIDATE(HttpStatus.FORBIDDEN, "해당 유저가 작성한 댓글이 아닙니다."); //질문 forbidden , notfound
    private final HttpStatus httpStatus;
    private final String message;
}

