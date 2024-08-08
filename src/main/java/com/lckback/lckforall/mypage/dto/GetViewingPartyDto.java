package com.lckback.lckforall.mypage.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

public class GetViewingPartyDto {

    @Builder
    @Getter
    public static class Response {

        private List<Information> viewingParties;
        private boolean isLast;
    }

    @Builder
    @Getter
    public static class Information {

        private Long id;
        private String name;
        private String date;
    }
}
