package com.lckback.lckforall.mypage.controller;

import com.lckback.lckforall.base.api.ApiResponse;
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
@CrossOrigin("*")
@RequestMapping("/my-pages")
@RequiredArgsConstructor
public class MyPageController {

	private final MyPageService myPageService;

	@GetMapping("/profiles")
	public ResponseEntity<?> getUserProfile(
		@RequestHeader(name = "userId") Long userId) {

		GetUserProfileDto.Response response =
			myPageService.getUserProfile(userId);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(response));
	}

	@PatchMapping("/withdrawal")
	public ResponseEntity<?> withdrawFromAccount(
		@RequestHeader(name = "userId") Long userId) {

		myPageService.withdrawFromAccount(userId);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccessWithNoContent());
	}

	@PatchMapping("/profiles")
	public ResponseEntity<?> updateUserProfile(
		@RequestHeader(name = "userId") Long userId,
		@RequestPart(required = false) MultipartFile profileImage,
		@RequestPart @Valid UpdateUserProfileDto.Request request) {

		UpdateUserProfileDto.Response response =
			myPageService.updateUserProfile(userId, profileImage, request);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(response));
	}

	@PatchMapping("/my-team")
	public ResponseEntity<?> updateMyTeam(
		@RequestHeader(name = "userId") Long userId,
		@RequestBody @Valid UpdateMyTeamDto.Request request) {

		myPageService.updateMyTeam(userId, request);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccessWithNoContent());
	}

	@GetMapping("/posts")
	public ResponseEntity<?> getUserPost(
		@RequestHeader(name = "userId") Long userId,
		@PageableDefault(size = 6) Pageable pageable) {

		GetUserPostDto.Response response = myPageService.getUserPost(userId, pageable);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(response));
	}

	@GetMapping("/comments")
	public ResponseEntity<?> getUseComment(
		@RequestHeader(name = "userId") Long userId,
		@PageableDefault(size = 6) Pageable pageable) {

		GetUserCommentDto.Response response = myPageService.getUserComment(userId, pageable);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(response));
	}

	@GetMapping("/viewing-parties/participate")
	public ResponseEntity<?> getUserViewingPartyAsParticipate(
		@RequestHeader(name = "userId") Long userId,
		@PageableDefault(size = 6) Pageable pageable) {

		GetViewingPartyDto.Response response =
			myPageService.getUserViewingPartyAsParticipate(userId, pageable);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(response));
	}

	@GetMapping("/viewing-parties/host")
	public ResponseEntity<?> getUserViewingPartyAsHost(
		@RequestHeader(name = "userId") Long userId,
		@PageableDefault(size = 6) Pageable pageable) {

		GetViewingPartyDto.Response response =
			myPageService.getUserViewingPartyAsHost(userId, pageable);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(response));
	}

	@DeleteMapping("/viewing-parties/participate")
	public ResponseEntity<?> cancelViewingPartyParticipation(
		@RequestHeader(name = "userId") Long userId,
		@RequestBody @Valid DeleteParticipationDto.Request request) {

		myPageService.cancelViewingPartyParticipation(userId, request);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccessWithNoContent());
	}

	@DeleteMapping("/viewing-parties/host")
	public ResponseEntity<?> cancelViewingPartyHosting(
		@RequestHeader(name = "userId") Long userId,
		@RequestBody @Valid DeleteViewingPartyDto.Request request) {

		myPageService.cancelViewingPartyHosting(userId, request);

		return ResponseEntity.ok()
			.body(ApiResponse.createSuccessWithNoContent());
	}
}
