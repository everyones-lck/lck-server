package com.lckback.lckforall.user.dto;

import org.springframework.web.multipart.MultipartFile;

import com.lckback.lckforall.team.model.Team;

import lombok.Builder;
import lombok.Getter;

public class SignupUserDataDto {

    @Getter
    @Builder
    public static class SignupUserData {

        private String kakaoUserId;
        private String nickName;
        private String role;
        private Long teamId;
    }

}
