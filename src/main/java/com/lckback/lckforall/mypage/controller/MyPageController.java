package com.lckback.lckforall.mypage.controller;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.mypage.dto.GetUserProfileDto;
import com.lckback.lckforall.mypage.service.MyPageService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
