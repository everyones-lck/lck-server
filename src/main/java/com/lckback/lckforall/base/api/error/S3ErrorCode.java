package com.lckback.lckforall.base.api.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum S3ErrorCode implements ErrorCode {

    EMPTY_FILE(HttpStatus.BAD_REQUEST, "파일이 비어있습니다."),
    IO_EXCEPTION_ON_IMAGE_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR,
        "이미지 업로드 중 IOException이 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
