package com.lckback.lckforall.user.dto;

import org.springframework.web.multipart.MultipartFile;

import com.lckback.lckforall.team.model.Team;

import lombok.Builder;
import lombok.Getter;

public class SignupUserDataDto {

    @Getter
    @Builder
    public static class SignupUserData {

        private Long id;
        private String kakaoUserId;
        private String nickName;
        private MultipartFile profileImage;
        private String role;
        private String status;
        private Team team;
    }

}
