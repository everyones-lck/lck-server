package com.lckback.lckforall.mypage.dto;

import lombok.Builder;

public class GetUserProfileDto {

    @Builder
    public static class Response {

        private String nickname;
        private String profileImageUrl;
        private String teamLogoUrl;
        private String tier;
    }
}
