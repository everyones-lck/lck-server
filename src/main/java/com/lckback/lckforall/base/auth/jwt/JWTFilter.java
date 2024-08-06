package com.lckback.lckforall.base.auth.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lckback.lckforall.base.api.error.TokenErrorCode;
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
        "/auth/signup",
        "/auth/login");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws
        ServletException,
        IOException {

        try {

            if (shouldExclude(request)) {

                log.info("shouldExclude: {}", request.getRequestURI());

                filterChain.doFilter(request, response);

                return;
            }

            String token = getTokenFromRequest(request);
            if (StringUtils.hasText(token) && jwtUtil.validateToken(token)) {
                String kakaoUserId = jwtUtil.getUserId(token);
                String role = jwtUtil.getRole(token);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(kakaoUserId, token, Collections.singleton(new SimpleGrantedAuthority(role)));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        } catch(Exception e) {
            request.setAttribute("exception", e);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {

        // 로그인 요청의 URL 패턴 확인String requestURI = request.getRequestURI();

        if (shouldExclude(request)) {
            // 로그인 요청인 경우 Authorization 헤더를 체크하지 않음
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
        return EXCLUDE_URLS.stream().anyMatch(url -> request.getRequestURI().equals(url));
    }
}





