package com.lckback.lckforall.community.controller;


import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.base.auth.service.AuthService;
import com.lckback.lckforall.community.dto.CommentDto;
import com.lckback.lckforall.community.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final AuthService authService;
    private final CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Void>> createComment(
            @RequestHeader("Authorization") String token,
            @RequestBody CommentDto.createCommentRequest request) {
        String kakaoUserId = authService.getKakaoUserId(token);
        commentService.createComment(request, kakaoUserId);

        return ResponseEntity.ok()
                .body(ApiResponse.createSuccessWithNoContent());
    }


}
