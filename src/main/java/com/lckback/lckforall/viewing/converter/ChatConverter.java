package com.lckback.lckforall.viewing.converter;


import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.viewing.dto.ChatDTO;
import com.lckback.lckforall.viewing.model.ChatMessage;
import com.lckback.lckforall.viewing.model.ChatRoom;
import com.lckback.lckforall.viewing.model.ViewingParty;
import org.springframework.data.domain.Page;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public class ChatConverter {

    public static ChatRoom toChatRoom() {
        return ChatRoom.builder().build();
    }

    public static ChatDTO.ChatRoomResponse toChatRoomResponse(ChatRoom chatRoom, ViewingParty viewingParty, Boolean isExist){
        return ChatDTO.ChatRoomResponse.builder()
                .roomId(chatRoom.getId())
                .viewingPartyName(viewingParty.getName())
                .isExist(isExist)
                .build();
    }

    public static ChatMessage toChatMessage(ChatDTO.Message chatMessage) {
        return ChatMessage.builder()
                .content(chatMessage.getMessage())
                .build();
    }

    public static ChatDTO.ChatMessageListResponse toChatMessageListResponse(Page<ChatMessage> chatMessageList, User user, ViewingParty viewingParty, Boolean isLast, Integer totalPage) {
        List<ChatDTO.ChatMessageResponse> chatList = chatMessageList.stream().map(ChatConverter::toChatMessageResponse).toList();
        return ChatDTO.ChatMessageListResponse.builder()
                .isLast(isLast)
                .totalPage(totalPage)
                .viewingPartyName(viewingParty.getName())
                .receiverName(user.getNickname())
                .receiverTeam(user.getTeam().getTeamName())
                .receiverProfileImage(user.getProfileImageUrl())
                .chatMessageList(chatList)
                .build();
    }

    public static ChatDTO.ChatMessageResponse toChatMessageResponse(ChatMessage chatMessage) {
        return ChatDTO.ChatMessageResponse.builder()
                .createdAt(chatMessage.getTime())
                .senderName(chatMessage.getSenderName())
                .message(chatMessage.getContent())
                .build();
    }
}
