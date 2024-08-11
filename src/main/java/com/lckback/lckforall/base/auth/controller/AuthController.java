package com.lckback.lckforall.base.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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
    public ResponseEntity<?> login(@RequestBody @Valid GetKakaoUserIdDto.Request request) {

        AuthResponseDto authResponseDto = authService.login(request);

        log.info("AuthResponseDto: {}", authResponseDto);

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
    public ResponseEntity<?> refresh(@RequestBody @Valid GetRefreshTokenDto.Request request) {

        AuthResponseDto authResponseDto = authService.refresh(request);



        return ResponseEntity.ok(com.lckback.lckforall.base.api.ApiResponse.createSuccess(authResponseDto));
    }

    @Operation(summary = "테스트 API", description = "테스트 API")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON201", description = "OK, 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TOKEN4001", description = "BAD_REQUEST, 잘못된 JWT 토큰입니다."),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TOKEN4001", description = "UNAUTHORIZED, 유효하지 않은 JWT 서명입니다."),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TOKEN4001", description = "BAD_REQUEST, Authorization 헤더가 존재하지 않습니다."),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TOKEN4001", description = "BAD_REQUEST, Bearer로 시작하지 않는 토큰입니다."),
    })
    @GetMapping("/test")
    public ResponseEntity<?> test(@RequestHeader("Authorization") String token) {

        return authService.testToken(token);
    }

    @Operation(summary = "유저 권한 접근 API", description = "유저 권한 접근 API")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON201", description = "OK, 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TOKEN4001", description = "BAD_REQUEST, 잘못된 JWT 토큰입니다."),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TOKEN4001", description = "UNAUTHORIZED, 유효하지 않은 JWT 서명입니다."),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TOKEN4001", description = "BAD_REQUEST, Authorization 헤더가 존재하지 않습니다."),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TOKEN4001", description = "BAD_REQUEST, Bearer로 시작하지 않는 토큰입니다."),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TOKEN4001", description = "UNAUTHORIZED, 만료된 Refresh 토큰입니다."),
    })
    @GetMapping("/users/test")
    public ResponseEntity<?> userTest(@RequestHeader("Authorization") String token) {

        return authService.testToken(token);
    }

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

// package com.lckback.lckforall.base.auth.controller;
//
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestHeader;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestPart;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.multipart.MultipartFile;
//
// import com.lckback.lckforall.base.api.ApiResponse;
// import com.lckback.lckforall.base.auth.dto.AuthResponseDto;
// import com.lckback.lckforall.base.auth.dto.GetKakaoUserIdDto;
// import com.lckback.lckforall.base.auth.dto.GetRefreshTokenDto;
// import com.lckback.lckforall.base.auth.jwt.JWTUtil;
// import com.lckback.lckforall.base.auth.service.AuthService;
// import com.lckback.lckforall.user.dto.SignupUserDataDto;
//
// import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;
//
// @RestController
// @RequestMapping("/auth")
// @RequiredArgsConstructor
// public class AuthController {
//
//     private final AuthService authService;
//     private final JWTUtil jWTUtil;
//
//     @PostMapping("/signup")
//     public ResponseEntity<?> signup(
//         @RequestPart(required = false) MultipartFile profileImage,
//         @RequestPart SignupUserDataDto.SignupUserData signupUserData) {
//
//         AuthResponseDto authResponseDto = authService.signup(profileImage, signupUserData);
//
//         return ResponseEntity.status(201).body(ApiResponse.createSuccess(authResponseDto));
//     }
//
//
//     @PostMapping("/login")
//     public ResponseEntity<?> login(@RequestBody @Valid GetKakaoUserIdDto.Request request) {
//
//         AuthResponseDto authResponseDto = authService.login(request);
//
//         return ResponseEntity.ok(ApiResponse.createSuccess(authResponseDto));
//     }
//
//     @PostMapping("/refresh")
//     public ResponseEntity<?> refresh(@RequestBody @Valid GetRefreshTokenDto.Request request) {
//
//         AuthResponseDto authResponseDto = authService.refresh(request);
//
//         return ResponseEntity.ok(ApiResponse.createSuccess(authResponseDto));
//     }
//
//     @GetMapping("/test")
//     public ResponseEntity<?> test(@RequestHeader("Authorization") String token) {
//
//         return authService.testToken(token);
//     }
//
//     @GetMapping("/admin/test")
//     public ResponseEntity<?> adminTest(@RequestHeader("Authorization") String token) {
//
//         return authService.testToken(token);
//     }
//
//     @GetMapping("/users/test")
//     public ResponseEntity<?> userTest(@RequestHeader("Authorization") String token) {
//
//         return authService.testToken(token);
//     }
// }
