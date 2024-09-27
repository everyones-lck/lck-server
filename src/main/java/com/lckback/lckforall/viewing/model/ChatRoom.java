package com.lckback.lckforall.viewing.model;

import com.lckback.lckforall.base.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Document(collection = "room")
public class ChatRoom {
    @Id
    private String id;

    private Long viewingPartyId;

    // 해당 뷰잉 파티에 질문하는 사람이 만든 방
    private Long userId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
