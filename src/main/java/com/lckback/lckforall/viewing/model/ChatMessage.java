package com.lckback.lckforall.viewing.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.lckback.lckforall.base.model.BaseEntity;
import com.lckback.lckforall.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
@Data
@Document(collection = "chat")
@EntityListeners(AuditingEntityListener.class)
public class ChatMessage {
    @Id
    private String id;

    private String roomId;

    private Long senderId;

    private String senderName;

    private String content;

    private Date time;
}
