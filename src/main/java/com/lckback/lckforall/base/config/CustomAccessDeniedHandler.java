package com.lckback.lckforall.base.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.lckback.lckforall.base.api.error.UserErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;

/**
 * 접근 거부 핸들러를 커스텀으로 처리하는 클래스.
 * Spring Security에서 접근 거부 예외가 발생할 때 호출됩니다.
 */
@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final HandlerExceptionResolver resolver;

    /**
     * 커스텀 접근 거부 핸들러 생성자.
     * @param resolver 예외 처리 리졸버
     */
    public CustomAccessDeniedHandler(
        @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * 접근 거부 예외 처리를 수행합니다.
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param accessDeniedException 접근 거부 예외
     * @throws IOException 입출력 예외 발생 시
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException {
        log.info("CustomAccessDeniedHandler 실행");


        // 예외가 null인지 확인
        if (accessDeniedException == null) {
            log.error("AccessDeniedException is null");

            resolver.resolveException(request, response, null,
                new RestApiException(UserErrorCode.USER_ACCESS_DENIED));
        }

        // 예외 처리 리졸버를 사용하여 예외를 처리합니다.
        resolver.resolveException(request, response, null,
            (Exception) request.getAttribute("exception"));

    }
}