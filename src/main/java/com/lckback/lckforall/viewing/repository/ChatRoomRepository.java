package com.lckback.lckforall.viewing.repository;

import com.lckback.lckforall.viewing.model.ChatMessage;
import com.lckback.lckforall.viewing.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Boolean existsByMessages(List<ChatMessage> chatMessages);
}
