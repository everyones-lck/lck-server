package com.lckback.lckforall.viewing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ViewingPartyListDTO {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseList {
        List<Response> partyList;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        Long id;
        String name;
        String userName;
        String photoURL;
        LocalDateTime partyDate;
        Double latitude;
        Double longitude;
        String location;
    }
}
