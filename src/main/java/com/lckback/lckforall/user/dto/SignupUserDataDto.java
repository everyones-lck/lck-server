package com.lckback.lckforall.user.dto;

import com.lckback.lckforall.base.type.UserRole;
import com.lckback.lckforall.base.type.UserStatus;
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
        private String profileImageUrl;
        private UserRole role;
        private UserStatus status;
        private Team team;
    }

}


