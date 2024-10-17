package com.lckback.lckforall.viewing.dto;

import lombok.*;

import java.util.List;

public class ParticipantListDTO {
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseList {
        Boolean isLast;
        Integer totalPage;
        String viewingPartyName;
        String ownerName;
        String ownerTeam;
        String ownerImage;
        List<Response> participantList;
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        String kakaoUserId;
        Long id;
        String name;
        String team;
        String image;
        Boolean isParticipating;
        Boolean isChatting;
    }
}
