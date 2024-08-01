package com.lckback.lckforall.base.api.handler;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.base.api.error.CommonErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(RestApiException.class)
    public ApiResponse<?> handleRestApiException(RestApiException e) {
        return ApiResponse.createFail(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException() {
        return ApiResponse.createFail(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }
}

