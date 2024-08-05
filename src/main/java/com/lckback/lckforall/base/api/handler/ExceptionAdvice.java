package com.lckback.lckforall.base.api.handler;

import java.security.SignatureException;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.base.api.error.CommonErrorCode;
import com.lckback.lckforall.base.api.error.ErrorCode;
import com.lckback.lckforall.base.api.error.TokenErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<Object> handleRestApiException(RestApiException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("handleIllegalArgument");
        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(errorCode, ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("handleHttpRequestMethodNotSupportedException");
        ErrorCode errorCode = CommonErrorCode.METHOD_NOT_ALLOWED;
        return handleExceptionInternal(errorCode);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        log.warn("MethodArgumentNotValidException ");
        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(errorCode, getDefaultMessage(e));
    }

    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException e) {
        log.warn("ConstraintViolationException");
        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(errorCode, e.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredAccessToken(ExpiredJwtException e) {
        log.warn("handle Expired Access Token");
        ErrorCode errorCode = TokenErrorCode.EXPIRED_ACCESS_TOKEN;
        return handleExceptionInternal(errorCode, errorCode.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredRefreshToken(ExpiredJwtException e) {
        log.warn("handle Expired Refresh Token");
        ErrorCode errorCode = TokenErrorCode.EXPIRED_REFRESH_TOKEN;
        return handleExceptionInternal(errorCode, errorCode.getMessage());
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<Object> handleMalformedJwt(MalformedJwtException e) {
        log.warn("handle MalformedJwtException");
        ErrorCode errorCode = TokenErrorCode.MALFORMED_JWT;
        return handleExceptionInternal(errorCode, errorCode.getMessage());
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<Object> handleSignature(SignatureException e) {
        log.warn("handle SignatureException");
        ErrorCode errorCode = TokenErrorCode.SIGNATURE_INVALID;
        return handleExceptionInternal(errorCode, errorCode.getMessage());
    }

    private static String getDefaultMessage(MethodArgumentNotValidException e) {
        StringBuilder message = new StringBuilder();
        for (ObjectError error : e.getBindingResult().getAllErrors()) {
            message.append(error.getDefaultMessage()).append("\u00a0");
        }
        return message.toString();
    }

    private ResponseEntity<Object> handleExceptionInternal(final ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ApiResponse.createFail(errorCode));
    }

    private ResponseEntity<Object> handleExceptionInternal(final ErrorCode errorCode, final String message) {
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ApiResponse.createFail(message));
    }
}

