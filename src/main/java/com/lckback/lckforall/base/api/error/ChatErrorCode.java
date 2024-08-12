package com.lckback.lckforall.base.api.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatErrorCode implements ErrorCode {

    CHAT_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방이 생성되어 있지 않습니다."),
    CHAT_ALREADY_EXIST(HttpStatus.ALREADY_REPORTED, "이미 채팅방이 생성되어 있습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
