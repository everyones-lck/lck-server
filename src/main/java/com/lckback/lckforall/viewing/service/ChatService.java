package com.lckback.lckforall.viewing.service;

import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.viewing.dto.ChatDTO;
import com.lckback.lckforall.viewing.model.ChatRoom;
import org.springframework.web.socket.WebSocketSession;

public interface ChatService {
    ChatDTO.ChatRoomResponse createParticipantChatRoom(String kakaoUserId, Long viewingPartyId);

    ChatDTO.ChatRoomResponse createOwnerChatRoom(String kakaoUserId, Long viewingPartyId, String participantKakaoUserId);


    public <T> void sendMessage(WebSocketSession session, T message);

    ChatDTO.ChatMessageListResponse getChatMessage(String kakaoUserId, Long roomId, Integer page, Integer size);

    User findUserOfChat(ChatDTO.Message chatMessage);

    ChatRoom findChatRoom(ChatDTO.Message chatMessage);

    void saveMessage(ChatDTO.Message chatMessage, String kakaoUserId);
}
