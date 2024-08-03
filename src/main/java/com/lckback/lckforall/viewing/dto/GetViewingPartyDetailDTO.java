package com.lckback.lckforall.viewing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class GetViewingPartyDetailDTO {
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        String name;
        String ownerName;
        String ownerTeam;
        String ownerImage;
        String qualify;
        LocalDateTime partyDate;
        String location;
        Integer price;
        Integer lowParticipate;
        Integer highParticipate;
        String etc;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ParticipateResponse {
        Long participantId;
        Long ownerId;
    }
}
