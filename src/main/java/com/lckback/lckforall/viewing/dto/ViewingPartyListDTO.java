package com.lckback.lckforall.viewing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ViewingPartyListDTO {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseList {
        Boolean isLast;
        Integer totalPage;
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
        String teamName;
        String photoURL;
        String partyDate;
        Double latitude;
        Double longitude;
        String location;
    }
}
