package com.lckback.lckforall.viewing.dto;

import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
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
            // 입장, 채팅, 나가기
            ENTER, TALK, OUT
        }

        private Message.MessageType type;
        private String chatRoomId;
        private String senderName;
        private String message;
    }

    @Getter
    public static class ChatRoomResponse {
        private String roomId;
        private String viewingPartyName;
        private Boolean isExist;
        private Set<WebSocketSession> sessions = new HashSet<>();

        @Builder
        public ChatRoomResponse(String roomId, String viewingPartyName, Boolean isExist) {
            this.roomId = roomId;
            this.viewingPartyName = viewingPartyName;
            this.isExist = isExist;
        }
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatMessageListResponse{
        Boolean isLast;
        Integer totalPage;
        String viewingPartyName;
        String receiverName;
        String receiverTeam;
        String receiverProfileImage;
        List<ChatMessageResponse> chatMessageList;
    }
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatMessageResponse{
        Date createdAt;
        String senderName;
        String message;
    }
}
