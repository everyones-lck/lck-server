package com.lckback.lckforall.base.auth.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lckback.lckforall.base.api.error.TokenErrorCode;
import com.lckback.lckforall.base.api.error.UserErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    private static final List<String> EXCLUDE_URLS = Arrays.asList(
        "/auth/nickname",
        "/auth/signup",
        "/auth/login",
        "/auth/refresh",
        "/swagger-ui/",
        "/v3/");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws
        ServletException,
        IOException {

        try {
            if (shouldExclude(request)) {

                // log.info("shouldExclude: {}", request.getRequestURI());
                filterChain.doFilter(request, response);

                return;
            }

            String token = getTokenFromRequest(request);

            if (StringUtils.hasText(token) && jwtUtil.validateToken(token)) {
                Authentication authentication = jwtUtil.getAuthentication(token);

                // 권한 검사 로직을 추가
                if (authentication.getAuthorities().isEmpty()) {
                    // 권한이 부족한 경우
                    log.warn("User does not have authorities, throwing USER_ACCESS_DENIED exception");
                    throw new RestApiException(UserErrorCode.USER_ACCESS_DENIED);
                }

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch(Exception e) {
            log.error("Exception in JWTFilter: {}", e.getMessage());
            request.setAttribute("exception", e);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage()); // 403 에러를 반환

            return; // 필터 체인 진행을 중단
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {

        if (shouldExclude(request)) {

            return null;
        }

        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null) {
            throw new RestApiException(TokenErrorCode.MISSING_AUTHORIZATION_HEADER);
        }

        if (!bearerToken.startsWith("Bearer ")) {
            throw new RestApiException(TokenErrorCode.INVALID_BEARER_PREFIX);
        }

        return bearerToken.substring(7);
    }

    private boolean shouldExclude(HttpServletRequest request) {
        log.info("shouldExclude: {}", request.getRequestURI());
        //return EXCLUDE_URLS.stream().anyMatch(url -> request.getRequestURI().equals(url));
        String requestURI = request.getRequestURI();
        log.info(requestURI);

        return EXCLUDE_URLS.stream().anyMatch(url -> requestURI.startsWith(url));
    }
}
