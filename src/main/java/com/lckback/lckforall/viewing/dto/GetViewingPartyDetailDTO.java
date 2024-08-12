package com.lckback.lckforall.viewing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class GetViewingPartyDetailDTO {
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        Boolean participated;
        String name;
        String ownerName;
        String ownerTeam;
        String ownerImage;
        String qualify;
        String partyDate;
        String location;
        Double latitude;
        Double longitude;
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
