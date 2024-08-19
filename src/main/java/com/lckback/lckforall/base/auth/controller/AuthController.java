package com.lckback.lckforall.base.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.base.auth.dto.AuthResponseDto;
import com.lckback.lckforall.base.auth.dto.GetKakaoUserIdDto;
import com.lckback.lckforall.base.auth.dto.GetNicknameDto;
import com.lckback.lckforall.base.auth.dto.GetRefreshTokenDto;
import com.lckback.lckforall.base.auth.jwt.JWTUtil;
import com.lckback.lckforall.base.auth.service.AuthService;
import com.lckback.lckforall.user.dto.SignupUserDataDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth" , description = "회원가입 및 로그인, 토큰재발행 관련")
@Slf4j
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;
    private final JWTUtil jWTUtil;

    @GetMapping("/nickname")
    @Operation(summary = "닉네임 중복 체크", description = "닉네임이 이미 존재하는지 확인합니다.")
    public ResponseEntity<ApiResponse<Boolean>> checkNickname(@PathVariable String nickName) {
        boolean available = authService.isNicknameAvailable(nickName);

        return ResponseEntity.ok(ApiResponse.createSuccess(available));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthResponseDto>> signup(
        @RequestPart(required = false) MultipartFile profileImage,
        @RequestPart SignupUserDataDto.SignupUserData signupUserData) {

        AuthResponseDto authResponseDto = authService.signup(profileImage, signupUserData);

        return ResponseEntity.status(201).body(ApiResponse.createSuccess(authResponseDto));
    }

    // @Operation(summary = "회원가입 API", description = "회원가입 API")
    // @ApiResponses({
    //     @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON201", description = "OK, 성공"),
    //     @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "CONFLICT, 사용자가 이미 존재합니다."),
    // })
    // @PostMapping("/signup")
    // public ResponseEntity<?> signup(
    //     @ModelAttribute SignupUserDataDto signupUserDataDto) {
    //
    //     AuthResponseDto authResponseDto = authService.signup(signupUserDataDto.getProfileImage(), signupUserDataDto.getSignupUserData());
    //
    //     return ResponseEntity.status(201).body(com.lckback.lckforall.base.api.ApiResponse.createSuccess(authResponseDto));
    // }

    @Operation(summary = "로그인 API", description = "로그인 API")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "UNAUTHORIZED, 유효하지 않은 카카오 유저 아이디입니다."),
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(@RequestBody @Valid GetKakaoUserIdDto.Request request) {

        AuthResponseDto authResponseDto = authService.login(request);

        // log.info("AuthResponseDto: {}", authResponseDto);

        return ResponseEntity.ok(com.lckback.lckforall.base.api.ApiResponse.createSuccess(authResponseDto));
    }

    @Operation(summary = "Refresh 토큰 재발행", description = "Refresh 토큰 재발행")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON201", description = "OK, 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TOKEN4001", description = "UNAUTHORIZED, 유효하지 않은 Refresh 토큰입니다."),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "UNAUTHORIZED, 유효하지 않은 카카오 유저 아이디입니다."),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TOKEN4002", description = "BAD_REQUEST, 잘못된 JWT 토큰입니다."),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TOKEN4003", description = "UNAUTHORIZED, 유효하지 않은 JWT 서명입니다."),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TOKEN4004", description = "BAD_REQUEST, Authorization 헤더가 존재하지 않습니다."),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TOKEN4005", description = "BAD_REQUEST, Bearer로 시작하지 않는 토큰입니다."),
    })
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponseDto>> refresh(@RequestBody @Valid GetRefreshTokenDto.Request request) {

        AuthResponseDto authResponseDto = authService.refresh(request);

        // log.info("AuthResponseDto: {}", authResponseDto);

        return ResponseEntity.ok(com.lckback.lckforall.base.api.ApiResponse.createSuccess(authResponseDto));
    }

    // 아래는 테스트 코드(user, admin 권한 데모데이 전까지는 불필요해서 주석처리)

    // @Operation(summary = "유저 권한 접근 API", description = "유저 권한 접근 API")
    // @ApiResponses({
    //     @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON201", description = "OK, 성공"),
    //     @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TOKEN4001", description = "BAD_REQUEST, 잘못된 JWT 토큰입니다."),
    //     @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TOKEN4001", description = "UNAUTHORIZED, 유효하지 않은 JWT 서명입니다."),
    //     @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TOKEN4001", description = "BAD_REQUEST, Authorization 헤더가 존재하지 않습니다."),
    //     @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TOKEN4001", description = "BAD_REQUEST, Bearer로 시작하지 않는 토큰입니다."),
    //     @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TOKEN4001", description = "UNAUTHORIZED, 만료된 Refresh 토큰입니다."),
    // })
    // @GetMapping("/users/test")
    // public ResponseEntity<?> userTest(@RequestHeader("Authorization") String token) {
    //
    //     return authService.testToken(token);
    // }

    // @GetMapping("/admin/test")
    // @Operation(summary = "관리자 권한 접근 API", description = "관리자 접근 API But 완성 X 이건 테스트 X")
    // @ApiResponses({
    //     @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON201", description = "OK, 성공"),
    //     @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "UNAUTHORIZED, 사용자가 이미 존재합니다."),
    // })
    // public ResponseEntity<?> adminTest(@RequestHeader("Authorization") String token) {
    //
    //     return authService.testToken(token);
    // }
}