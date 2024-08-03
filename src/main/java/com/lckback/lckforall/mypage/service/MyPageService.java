package com.lckback.lckforall.mypage.service;

import com.lckback.lckforall.base.api.error.CommonErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;
import com.lckback.lckforall.mypage.dto.GetUserProfileDto;
import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.user.respository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;

    public GetUserProfileDto.Response getUserProfile(Long userId) {

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));

        return GetUserProfileDto.Response.builder()
            .nickname(user.getNickname())
            .profileImageUrl(user.getProfileImageUrl())
            .teamLogoUrl(user.getTeam().getTeamLogoUrl())
            .tier("temp")
            .build();
    }

	public void logout(String accessToken, String refreshToken) {

       /* String user의_고유_식별_값 = accessToken claim에서 뽑아오기;
        Long accessToken_만료_기간 = ~~~;
        redis에 blacklist 설정(accessToken_만료_기간, accessToken, "logout");

        if(redis.hasKey(user의_고유_식별_값)){
            redis.deleteRefreshToken(refreshToken);
        }else{
            throw new RestApiException()
        }*/
	}
}
