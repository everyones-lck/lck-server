package com.lckback.lckforall.base.auth.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.base.auth.dto.AuthResponseDto;
import com.lckback.lckforall.base.auth.dto.GetKakaoUserIdDto;
import com.lckback.lckforall.base.auth.dto.GetRefreshTokenDto;
import com.lckback.lckforall.base.auth.jwt.JWTUtil;
import com.lckback.lckforall.base.auth.service.AuthService;
import com.lckback.lckforall.user.dto.SignupUserDataDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JWTUtil jWTUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
        @RequestPart(required = false) MultipartFile profileImage,
        @RequestPart SignupUserDataDto.SignupUserData signupUserData) {

        AuthResponseDto authResponseDto = authService.signup(profileImage, signupUserData);
        return ResponseEntity.status(201).body(ApiResponse.createSuccess(authResponseDto));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid GetKakaoUserIdDto.Request request) {

        AuthResponseDto authResponseDto = authService.login(request);

        return ResponseEntity.ok(ApiResponse.createSuccess(authResponseDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody @Valid GetRefreshTokenDto.Request request) {

        AuthResponseDto authResponseDto = authService.refresh(request);

        return ResponseEntity.ok(ApiResponse.createSuccess(authResponseDto));
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(@RequestHeader("Authorization") String token) {
        return authService.testToken(token);

    }
}
