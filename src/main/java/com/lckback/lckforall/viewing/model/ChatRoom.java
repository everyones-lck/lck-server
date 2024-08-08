package com.lckback.lckforall.viewing.model;

import com.lckback.lckforall.base.model.BaseEntity;
import com.lckback.lckforall.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "viewing_party_id", nullable = false)
    private ViewingParty viewingParty;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> messages = new ArrayList<>();

    public void setViewingParty(ViewingParty viewingParty) {
        if(this.viewingParty != null) {
            viewingParty.getChatRooms().remove(this);
        }
        this.viewingParty = viewingParty;
        viewingParty.getChatRooms().add(this);
    }
}
