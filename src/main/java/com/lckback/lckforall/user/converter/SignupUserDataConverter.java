package com.lckback.lckforall.user.converter;

import com.lckback.lckforall.user.dto.SignupUserDataDto;
import com.lckback.lckforall.user.model.User;

public class SignupUserDataConverter {

    // 회원가입 성공한 유저 데이터를 다시 클라이언트로 보내는 로직
    // 현재 쓰지 않는 데이터라 주석처리해둠
    //    public static SignupUserDataDto.SignupUserData convertToSignupUserData(User user) {
    //        return SignupUserDataDto.SignupUserData.builder()
    //            .id(user.getId())
    //            .kakaoUserId(user.getKakaoUserId())
    //            .nickName(user.getNickname())
    //            .profileImageUrl(user.getProfileImageUrl())
    //            .role(user.getRole())
    //            .status(user.getStatus())
    //            .team(user.getTeam())
    //            .build();
    //    }

    // SignupUserData DTO 객체를 User 엔티티 객체로 변환하기 위해 사용
    // 클라이언트로부터 전달받은 회원가입 데이터를 데이터베이스에 저장하기 위해 User 엔티티로 변환할 때 사용
    public static User convertToUser(SignupUserDataDto.SignupUserData signupUserData) {

        return User.builder()
            .id(signupUserData.getId())
            .kakaoUserId(signupUserData.getKakaoUserId())
            .nickname(signupUserData.getNickName())
            .profileImageUrl(signupUserData.getProfileImageUrl())
            .role(signupUserData.getRole())
            .status(signupUserData.getStatus())
            .team(signupUserData.getTeam())
            .build();
    }
}

