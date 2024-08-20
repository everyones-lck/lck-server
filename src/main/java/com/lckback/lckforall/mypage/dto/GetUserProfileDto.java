package com.lckback.lckforall.mypage.dto;

import lombok.Builder;
import lombok.Getter;

public class GetUserProfileDto {

    @Builder
    @Getter
    public static class Response {

        private String nickname;
        private String profileImageUrl;
        private Long teamId;
        private String tier;
    }
}
