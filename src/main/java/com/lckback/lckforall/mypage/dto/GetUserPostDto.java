package com.lckback.lckforall.mypage.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class GetUserPostDto {

    @Builder
    @Getter
    public static class Response {

        private List<Information> posts;
        private Boolean isLast;
    }

    @Builder
    @Getter
    @Setter
    public static class Information {

        private Long id;
        private String title;
        private String postType;
    }
}
