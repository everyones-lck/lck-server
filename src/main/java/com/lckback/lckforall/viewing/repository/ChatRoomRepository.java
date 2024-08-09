package com.lckback.lckforall.viewing.repository;

import com.lckback.lckforall.viewing.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
