package com.lckback.lckforall.viewing.dto;

import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatDTO {

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message {

        public enum MessageType {
            // 입장, 채팅
            ENTER, TALK, OUT
        }

        private Message.MessageType type;
        private Long chatRoomId;
        private Long senderId;
        private String message;
    }

    @Getter
    public static class ChatRoomResponse {
        private Long roomId;
        private String viewingPartyName;
        private Set<WebSocketSession> sessions = new HashSet<>();

        @Builder
        public ChatRoomResponse(Long roomId, String viewingPartyName) {
            this.roomId = roomId;
            this.viewingPartyName = viewingPartyName;
        }
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatMessageListResponse{
        String viewingPartyName;
        String receiverName;
        String receiverTeam;
        List<ChatMessageResponse> chatMessageList;
    }
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatMessageResponse{
        Long senderId;
        String message;
    }
}
