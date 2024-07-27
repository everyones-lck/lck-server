package com.lckback.lckforall.base.api.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum VoteErrorCode implements ErrorCode{

    THERE_IS_NO_VOTE(HttpStatus.NOT_FOUND, "아직 투표 결과가 없습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
