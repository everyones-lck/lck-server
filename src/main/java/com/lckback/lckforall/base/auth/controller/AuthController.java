package com.lckback.lckforall.base.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.base.auth.dto.AuthResponseDto;
import com.lckback.lckforall.base.auth.service.AuthService;
import com.lckback.lckforall.user.dto.SignupUserDataDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
        @RequestPart(required = false) MultipartFile profileImage,
        @RequestPart SignupUserDataDto.SignupUserData signupUserData) {

        AuthResponseDto authResponseDto = authService.signup(profileImage, signupUserData);
        return ResponseEntity.status(201).body(ApiResponse.createSuccess(authResponseDto));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody String kakaoUserId) {

        AuthResponseDto authResponseDto = authService.login(kakaoUserId);

        return ResponseEntity.ok(ApiResponse.createSuccess(authResponseDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody String kakaoUserId) {

        AuthResponseDto authResponseDto = authService.refresh(kakaoUserId);

        return ResponseEntity.ok(ApiResponse.createSuccess(authResponseDto));
    }
}
