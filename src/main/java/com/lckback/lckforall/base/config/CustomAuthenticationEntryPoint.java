package com.lckback.lckforall.base.config;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * 사용자 인증 진입 지점 커스텀 처리 클래스.
 * 인증되지 않은 사용자의 요청을 처리할 때 호출되는 클래스입니다.
 */
@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;

    /**
     * 커스텀 인증 진입 지점 생성자.
     * @param resolver 예외 처리 리졸버
     */
    public CustomAuthenticationEntryPoint(
        @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * 인증 진입 지점에서 인증 예외 처리를 수행합니다.
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param authException 인증 예외
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException {
        log.info("JwtAuthenticationEntryPoint 실행");

        // 예외 처리 리졸버를 사용하여 예외를 처리합니다.
        resolver.resolveException(request, response, null,
            (Exception) request.getAttribute("exception"));

    }

}
