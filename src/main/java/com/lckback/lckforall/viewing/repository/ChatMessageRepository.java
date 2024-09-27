package com.lckback.lckforall.viewing.repository;

import com.lckback.lckforall.viewing.model.ChatMessage;
import com.lckback.lckforall.viewing.model.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import reactor.core.publisher.Flux;

public interface ChatMessageRepository extends MongoRepository<ChatMessage,String> {
    Page<ChatMessage> findAllByRoomIdOrderByTimeDesc(String chatRoomId, Pageable pageable);
}
