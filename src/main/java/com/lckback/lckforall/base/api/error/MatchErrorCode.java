package com.lckback.lckforall.base.api.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MatchErrorCode implements ErrorCode{
    THERE_IS_NO_MATCH_TODAY(HttpStatus.NOT_FOUND, "오늘 경기는 없습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
