package com.lckback.lckforall.base.api.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "파라미터가 올바르지 않습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없는 리소스입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않는 HTTP Method입니다."),

    // 사용자 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을수 없습니다."),

    // 뷰잉파티 관련
    PARTY_NOT_FOUND(HttpStatus.NOT_FOUND, "뷰잉파티를 찾을수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
