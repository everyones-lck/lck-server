package com.lckback.lckforall.mypage.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

public class GetUserCommentDto {

    @Builder
    @Getter
    public static class Response {

        private List<Information> comments;
        private Boolean isLast;
    }

    @Builder
    public static class Information {

        private Long id;
        private String content;
        private String postType;
    }
}
