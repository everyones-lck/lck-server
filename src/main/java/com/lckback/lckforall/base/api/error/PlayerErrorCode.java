package com.lckback.lckforall.base.api.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PlayerErrorCode implements ErrorCode{
    THERE_IS_NO_SUCH_PLAYER(HttpStatus.NOT_FOUND, "해당 선수는 존재하지 않습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
