package com.lckback.lckforall.base.api.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    ALREADY_EXIST_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    NOT_EXIST_USER(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
