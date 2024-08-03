package com.lckback.lckforall.mypage.service;

import com.lckback.lckforall.base.api.error.CommonErrorCode;
import com.lckback.lckforall.base.api.error.UserErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;
import com.lckback.lckforall.mypage.dto.GetUserProfileDto;
import com.lckback.lckforall.mypage.dto.UpdateUserProfileDto;
import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.user.respository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

	public UpdateUserProfileDto.Response updateUserProfile(
		Long userId,
		MultipartFile profileImage,
		UpdateUserProfileDto.Request request) {

		if (profileImage.isEmpty() && request.getNickname() == null) {
			throw new RestApiException(CommonErrorCode.INVALID_PARAMETER);
		}

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));

		if (!profileImage.isEmpty()) {
			String updatedProfileImageUrl = "temp"; // s3 도입 전까지 임시
			user.updateProfileImageUrl(updatedProfileImageUrl);
		}

		if (request.getNickname() != null) {

			if (userRepository.existsByNickname(request.getNickname())) {
				throw new RestApiException(UserErrorCode.ALREADY_EXIST_NICKNAME);
			}

			user.updateNickname(request.getNickname());
		}

		return UpdateUserProfileDto.Response.builder()
			.updatedNickname(user.getNickname())
			.updatedProfileImageUrl(user.getProfileImageUrl())
			.build();
	}
}
