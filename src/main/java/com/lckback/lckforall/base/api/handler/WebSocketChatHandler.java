package com.lckback.lckforall.base.api.handler;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.lckback.lckforall.base.auth.service.AuthService;
import com.lckback.lckforall.viewing.dto.ChatDTO;
import com.lckback.lckforall.viewing.model.ChatRoom;

import com.lckback.lckforall.viewing.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {

    // json 형식 변환
    private final ObjectMapper objectMapper;
    private final ChatService chatService;
    private final AuthService authService;

    // 소켓 세션 저장
    private final Set<WebSocketSession> sessions = new HashSet<>();

    // {채팅방 ID, 소켓 세션}
    private final Map<Long, Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("{} 연결됨", session.getId());
        sessions.add(session);
    }

    // 메세지 전송 담당
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload: {}", payload);

        ChatDTO.Message chatMessage = objectMapper.readValue(payload, ChatDTO.Message.class);
        log.info("chatMessage: {}", chatMessage.toString());

        handlerActions(session, chatMessage);
    }

    // 메세지 전송시 처리 핸들러
    public void handlerActions(WebSocketSession session, ChatDTO.Message chatMessage) {
        ChatRoom chatRoom = chatService.findChatRoom(chatMessage);
        String authorization = session.getHandshakeHeaders().getFirst("Authorization");
        String kakaoUserId = authService.getKakaoUserId(authorization);
//        User user = chatService.findUserOfChat(chatMessage);
        // 메세지 타입에 따라 분기
        if (chatMessage.getType().equals(ChatDTO.Message.MessageType.ENTER)) {
            // 입장 메세지
            sessions.add(session);
            chatMessage.setMessage("%s 님이 입장했습니다.".formatted(chatMessage.getSenderName()));

        } else if (chatMessage.getType().equals(ChatDTO.Message.MessageType.OUT)) {
            // 퇴장 메세지
            sessions.remove(session);
            chatMessage.setMessage("%s 님이 퇴장했습니다.".formatted(chatMessage.getSenderName()));
        }
        sendMessage(chatMessage, chatService);

        // 저장될 메세지
        chatService.saveMessage(chatMessage, kakaoUserId);
    }

    private <T> void sendMessage(T message, ChatService chatService) {
        sessions.parallelStream()
                .forEach(session -> chatService.sendMessage(session, message));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("{} 연결 끊김", session.getId());
        sessions.remove(session);
    }
}
