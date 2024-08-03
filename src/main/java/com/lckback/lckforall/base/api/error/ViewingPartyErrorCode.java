package com.lckback.lckforall.base.api.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ViewingPartyErrorCode implements ErrorCode {
    PARTY_NOT_FOUND(HttpStatus.NOT_FOUND, "뷰잉파티를 찾을수 없습니다."),
    FAILED_PARTICIPATE_PARTY(HttpStatus.NOT_ACCEPTABLE, "뷰잉파티 참여에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
