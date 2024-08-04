package com.lckback.lckforall.base.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.base.api.error.UserErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {

            ApiResponse<?> failResponse = ApiResponse.createFail(UserErrorCode.USER_ACCESS_DENIED);

            return ResponseEntity.status(UserErrorCode.USER_ACCESS_DENIED.getHttpStatus())
                .body(failResponse.getMessage());
        }

        String kakaoUserId = authentication.getPrincipal().toString();
        ApiResponse<String> successResponse = ApiResponse.createSuccess("Access granted for admin: " + kakaoUserId);
        return ResponseEntity.ok(successResponse.getMessage());
    }
}


