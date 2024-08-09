package com.lckback.lckforall.viewing.model;

import com.lckback.lckforall.base.model.BaseEntity;
import com.lckback.lckforall.user.model.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String content;

    public void setChatRoom(ChatRoom chatRoom) {
        if(this.chatRoom != null) {
            chatRoom.getMessages().remove(this);
        }
        this.chatRoom = chatRoom;
        chatRoom.getMessages().add(this);
    }

    public void setUser(User user) {
        if(this.user != null) {
            user.getChatMessages().remove(this);
        }
        this.user = user;
        user.getChatMessages().add(this);
    }

}
