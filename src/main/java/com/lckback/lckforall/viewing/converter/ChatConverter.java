package com.lckback.lckforall.viewing.converter;


import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.viewing.dto.ChatDTO;
import com.lckback.lckforall.viewing.model.ChatMessage;
import com.lckback.lckforall.viewing.model.ChatRoom;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class ChatConverter {

    public static ChatRoom toChatRoom() {
        return ChatRoom.builder().build();
    }

    public static ChatDTO.ChatRoomResponse toChatRoomResponse(ChatRoom chatRoom){
        return ChatDTO.ChatRoomResponse.builder()
                .roomId(chatRoom.getId())
                .viewingPartyName(chatRoom.getViewingParty().getName())
                .build();
    }

    public static ChatMessage toChatMessage(ChatDTO.Message chatMessage) {
        return ChatMessage.builder()
                .content(chatMessage.getMessage())
                .build();
    }

    public static ChatDTO.ChatMessageListResponse toChatMessageListResponse(Page<ChatMessage> chatMessageList, User user, ChatRoom chatRoom) {
        List<ChatDTO.ChatMessageResponse> chatList = chatMessageList.stream().map(ChatConverter::toChatMessageResponse).toList();
        return ChatDTO.ChatMessageListResponse.builder()
                .viewingPartyName(chatRoom.getViewingParty().getName())
                .receiverName(user.getNickname())
                .receiverTeam(user.getTeam().getTeamName())
                .chatMessageList(chatList)
                .build();
    }

    public static ChatDTO.ChatMessageResponse toChatMessageResponse(ChatMessage chatMessage) {
        return ChatDTO.ChatMessageResponse.builder()
                .senderId(chatMessage.getUser().getId())
                .message(chatMessage.getContent())
                .build();
    }
}
