package com.lckback.lckforall.mypage.controller;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.base.auth.service.AuthService;
import com.lckback.lckforall.mypage.dto.DeleteParticipationDto;
import com.lckback.lckforall.mypage.dto.DeleteViewingPartyDto;
import com.lckback.lckforall.mypage.dto.GetUserCommentDto;
import com.lckback.lckforall.mypage.dto.GetUserPostDto;
import com.lckback.lckforall.mypage.dto.GetUserProfileDto;
import com.lckback.lckforall.mypage.dto.GetViewingPartyDto;
import com.lckback.lckforall.mypage.dto.UpdateMyTeamDto;
import com.lckback.lckforall.mypage.dto.UpdateUserProfileDto;
import com.lckback.lckforall.mypage.service.MyPageService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/my-pages")
@RequiredArgsConstructor
public class MyPageController {

	private final MyPageService myPageService;

	private final AuthService authService;

	@GetMapping("/profiles")
	public ResponseEntity<?> getUserProfile(
		@RequestHeader(name = "Authorization") String accessToken) {

		String kakaoUserId = authService.getKakaoUserId(accessToken);

		GetUserProfileDto.Response response =
			myPageService.getUserProfile(kakaoUserId);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(response));
	}

	@PatchMapping("/withdrawal")
	public ResponseEntity<?> withdrawFromAccount(
		@RequestHeader(name = "Authorization") String accessToken) {

		String kakaoUserId = authService.getKakaoUserId(accessToken);

		myPageService.withdrawFromAccount(kakaoUserId);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccessWithNoContent());
	}

	@PatchMapping("/profiles")
	public ResponseEntity<?> updateUserProfile(
		@RequestHeader(name = "Authorization") String accessToken,
		@RequestPart(required = false) MultipartFile profileImage,
		@RequestPart @Valid UpdateUserProfileDto.Request request) {

		String kakaoUserId = authService.getKakaoUserId(accessToken);

		UpdateUserProfileDto.Response response =
			myPageService.updateUserProfile(kakaoUserId, profileImage, request);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(response));
	}

	@PatchMapping("/my-team")
	public ResponseEntity<?> updateMyTeam(
		@RequestHeader(name = "Authorization") String accessToken,
		@RequestBody @Valid UpdateMyTeamDto.Request request) {

		String kakaoUserId = authService.getKakaoUserId(accessToken);

		myPageService.updateMyTeam(kakaoUserId, request);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccessWithNoContent());
	}

	@GetMapping("/posts")
	public ResponseEntity<?> getUserPost(
		@RequestHeader(name = "Authorization") String accessToken,
		@PageableDefault(size = 6) Pageable pageable) {

		String kakaoUserId = authService.getKakaoUserId(accessToken);

		GetUserPostDto.Response response = myPageService.getUserPost(kakaoUserId, pageable);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(response));
	}

	@GetMapping("/comments")
	public ResponseEntity<?> getUserComment(
		@RequestHeader(name = "Authorization") String accessToken,
		@PageableDefault(size = 6) Pageable pageable) {

		String kakaoUserId = authService.getKakaoUserId(accessToken);

		GetUserCommentDto.Response response = myPageService.getUserComment(kakaoUserId, pageable);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(response));
	}

	@GetMapping("/viewing-parties/participate")
	public ResponseEntity<?> getUserViewingPartyAsParticipate(
		@RequestHeader(name = "Authorization") String accessToken,
		@PageableDefault(size = 6) Pageable pageable) {

		String kakaoUserId = authService.getKakaoUserId(accessToken);

		GetViewingPartyDto.Response response =
			myPageService.getUserViewingPartyAsParticipate(kakaoUserId, pageable);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(response));
	}

	@GetMapping("/viewing-parties/host")
	public ResponseEntity<?> getUserViewingPartyAsHost(
		@RequestHeader(name = "Authorization") String accessToken,
		@PageableDefault(size = 6) Pageable pageable) {

		String kakaoUserId = authService.getKakaoUserId(accessToken);

		GetViewingPartyDto.Response response =
			myPageService.getUserViewingPartyAsHost(kakaoUserId, pageable);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(response));
	}

	@DeleteMapping("/viewing-parties/participate")
	public ResponseEntity<?> cancelViewingPartyParticipation(
		@RequestHeader(name = "Authorization") String accessToken,
		@RequestBody @Valid DeleteParticipationDto.Request request) {

		String kakaoUserId = authService.getKakaoUserId(accessToken);

		myPageService.cancelViewingPartyParticipation(kakaoUserId, request);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccessWithNoContent());
	}

	@DeleteMapping("/viewing-parties/host")
	public ResponseEntity<?> cancelViewingPartyHosting(
		@RequestHeader(name = "Authorization") String accessToken,
		@RequestBody @Valid DeleteViewingPartyDto.Request request) {

		String kakaoUserId = authService.getKakaoUserId(accessToken);

		myPageService.cancelViewingPartyHosting(kakaoUserId, request);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccessWithNoContent());
	}
	@DeleteMapping("/logout")
	public ResponseEntity<?> logout(
		@RequestHeader(name = "Authorization") String accessToken,
		@RequestHeader(name = "Refresh") String refreshToken) {

		String kakaoUserId = authService.getKakaoUserId(accessToken);

		myPageService.logout(kakaoUserId, refreshToken);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccessWithNoContent());
	}
}
