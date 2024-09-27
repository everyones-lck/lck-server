package com.lckback.lckforall.viewing.repository;

import com.lckback.lckforall.viewing.model.ChatMessage;
import com.lckback.lckforall.viewing.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {

//    Boolean existsByMessages(List<ChatMessage> chatMessages);
    Optional<ChatRoom> findByUserIdAndViewingPartyId(Long viewingPartyId, Long userId);
}
