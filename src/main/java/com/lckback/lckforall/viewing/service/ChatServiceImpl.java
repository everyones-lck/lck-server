package com.lckback.lckforall.viewing.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lckback.lckforall.base.api.error.ChatErrorCode;
import com.lckback.lckforall.base.api.error.UserErrorCode;
import com.lckback.lckforall.base.api.error.ViewingPartyErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;
import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.user.respository.UserRepository;
import com.lckback.lckforall.viewing.converter.ChatConverter;
import com.lckback.lckforall.viewing.dto.ChatDTO;
import com.lckback.lckforall.viewing.model.ChatMessage;
import com.lckback.lckforall.viewing.model.ChatRoom;
import com.lckback.lckforall.viewing.model.Participate;
import com.lckback.lckforall.viewing.model.ViewingParty;
import com.lckback.lckforall.viewing.repository.ChatMessageRepository;
import com.lckback.lckforall.viewing.repository.ChatRoomRepository;
import com.lckback.lckforall.viewing.repository.ParticipateRepository;
import com.lckback.lckforall.viewing.repository.ViewingPartyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    // json 형식 변환
    private final ObjectMapper objectMapper;

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ViewingPartyRepository viewingPartyRepository;
    private final ParticipateRepository participateRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    @Transactional
    public ChatDTO.ChatRoomResponse createParticipantChatRoom(String kakaoUserId, Long viewingPartyId) {

        User user = userRepository.findByKakaoUserId(kakaoUserId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        ViewingParty viewingParty = viewingPartyRepository.findById(viewingPartyId).orElseThrow(() -> new RestApiException(ViewingPartyErrorCode.PARTY_NOT_FOUND));
        Participate participate = participateRepository.findByUserAndViewingParty(user, viewingParty).orElseThrow(() -> new RestApiException(ViewingPartyErrorCode.PARTICIPATE_NOT_FOUND));

        if(participate.getChatRoom() != null){
            return ChatConverter.toChatRoomResponse(participate.getChatRoom(), true);
        }

        ChatRoom chatRoom = ChatConverter.toChatRoom();
        chatRoom.setViewingParty(viewingParty);
        participate.setChatRoom(chatRoom);
        chatRoomRepository.save(chatRoom);
        return ChatConverter.toChatRoomResponse(chatRoom, false);
    }

    @Override
    @Transactional
    public ChatDTO.ChatRoomResponse createOwnerChatRoom(String kakaoUserId, Long viewingPartyId, String participantKakaoUserId) {
        User owner = userRepository.findByKakaoUserId(kakaoUserId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        User participant = userRepository.findByKakaoUserId(participantKakaoUserId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        ViewingParty viewingParty = viewingPartyRepository.findById(viewingPartyId).orElseThrow(() -> new RestApiException(ViewingPartyErrorCode.PARTY_NOT_FOUND));
        Participate participate = participateRepository.findByUserAndViewingParty(participant, viewingParty).orElseThrow(() -> new RestApiException(ViewingPartyErrorCode.PARTICIPATE_NOT_FOUND));

        if(participate.getChatRoom() != null){
            return ChatConverter.toChatRoomResponse(participate.getChatRoom(), true);
        }

        ChatRoom chatRoom = ChatConverter.toChatRoom();
        chatRoom.setViewingParty(viewingParty);
        participate.setChatRoom(chatRoom);
        chatRoomRepository.save(chatRoom);
        return ChatConverter.toChatRoomResponse(chatRoom, false);

    }

    @Override
    @Transactional
    public <T> void sendMessage(WebSocketSession session, T message) {
        try{
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public ChatDTO.ChatMessageListResponse getChatMessage(String kakaoUserId, Long roomId, Integer page, Integer size) {
        User receiver;
        User sender = userRepository.findByKakaoUserId(kakaoUserId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new RestApiException(ChatErrorCode.CHAT_NOT_FOUND));
        User owner = chatRoom.getViewingParty().getUser();
        if(owner == sender) {
            Set<User> chatUserSet = chatRoom.getMessages()
                    .stream()
                    .map(m -> m.getUser() != owner ? m.getUser() : null)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            receiver = chatUserSet.iterator().next();
        }
        else{
            receiver = owner;
        }
        Page<ChatMessage> messages = chatMessageRepository.findAllByChatRoomOrderByCreatedAtDesc(chatRoom, PageRequest.of(page, size));
        int totalPage = messages.getTotalPages();
        boolean isLast = false;
        if(page == totalPage - 1){
            isLast = true;
        }
        return ChatConverter.toChatMessageListResponse(messages, receiver, chatRoom, isLast, totalPage);
    }

    @Override
    @Transactional
    public User findUserOfChat(ChatDTO.Message chatMessage) {
        return userRepository.findByKakaoUserId(chatMessage.getSenderName()).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public ChatRoom findChatRoom(ChatDTO.Message chatMessage) {
        return chatRoomRepository.findById(chatMessage.getChatRoomId()).orElseThrow(() -> new RestApiException(ChatErrorCode.CHAT_NOT_FOUND));
    }

    @Override
    @Transactional
    public void saveMessage(ChatDTO.Message chatMessage, String kakaoUserId) {
        User user = userRepository.findByKakaoUserId(kakaoUserId).get();
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessage.getChatRoomId()).get();
        ChatMessage saveMessage = ChatConverter.toChatMessage(chatMessage);
        saveMessage.setUser(user);
        saveMessage.setChatRoom(chatRoom);
        chatMessageRepository.save(saveMessage);
    }
}
