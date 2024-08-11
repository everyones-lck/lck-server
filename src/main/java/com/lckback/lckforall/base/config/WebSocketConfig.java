package com.lckback.lckforall.base.config;

import com.lckback.lckforall.base.auth.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketHandler webSocketHandler;
    private final JWTUtil jwtUtil;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // /chat 경로로 웹소켓 연결허용
        registry.addHandler(webSocketHandler, "/chat").setAllowedOrigins("*")
                .addInterceptors(new HttpSessionHandshakeInterceptor())    // HTTP 세션 정보 -> Web Socket 세션으로 전달
                .addInterceptors(new CustomHandshakeInterceptor(jwtUtil)); // JWT 검증 인터셉터 추가



    }
}
