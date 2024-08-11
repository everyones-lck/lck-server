package com.lckback.lckforall.community.controller;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.base.auth.service.AuthService;
import com.lckback.lckforall.community.dto.PostDto;
import com.lckback.lckforall.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    private final AuthService authService;

    /**
     * 전체 글 조회 (10개 페이징)
     * http://localhost:8080/posts?page=10&sort=createDate
     */
    @GetMapping("/list")
    public ResponseEntity<?> getPosts(Pageable pageable, @RequestParam String postType) {

        PostDto.PostListResponse data = postService.findPosts(pageable, postType);

        return ResponseEntity.ok()
                .body(ApiResponse.createSuccess(data));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPost(
            @RequestPart List<MultipartFile> files,
            @RequestBody PostDto.CreatePostRequest request,
            @RequestHeader("Authorization") String token) {
        String kakaoUserId = authService.getKakaoUserId(token);
        postService.createPost(files, request, kakaoUserId);

        return ResponseEntity.ok()
                .body(ApiResponse.createSuccessWithNoContent());
    }

    @GetMapping("/type-list")
    public ResponseEntity<?> getPostTypes(
            @RequestHeader("Authorization") String token) {
        PostDto.PostTypeListResponse response = postService.getPostTypes();
        return ResponseEntity.ok()
                .body(ApiResponse.createSuccess(response));

    }

}
