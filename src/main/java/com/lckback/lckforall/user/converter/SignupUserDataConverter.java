package com.lckback.lckforall.user.converter;

import java.time.LocalDateTime;

import com.lckback.lckforall.base.type.UserRole;
import com.lckback.lckforall.base.type.UserStatus;
import com.lckback.lckforall.team.model.Team;
import com.lckback.lckforall.user.dto.SignupUserDataDto;
import com.lckback.lckforall.user.model.User;

public class SignupUserDataConverter {

    // SignupUserData DTO 객체를 User 엔티티 객체로 변환하기 위해 사용
    // 클라이언트로부터 전달받은 회원가입 데이터를 데이터베이스에 저장하기 위해 User 엔티티로 변환할 때 사용
    public static User convertToUser(SignupUserDataDto.SignupUserData signupUserData, String profileImageUrl, Team team) {
        UserRole role = UserRole.valueOf(signupUserData.getRole().toUpperCase());

        return User.builder()
            .lastUpdatedMyTeam(LocalDateTime.now())
            .kakaoUserId(signupUserData.getKakaoUserId())
            .nickname(signupUserData.getNickName())
            .profileImageUrl(profileImageUrl)
            .role(role)
            .status(UserStatus.ACTIVE)
            .team(team)
            .build();
    }
}

