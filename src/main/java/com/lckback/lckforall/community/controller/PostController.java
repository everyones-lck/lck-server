package com.lckback.lckforall.community.controller;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.base.auth.service.AuthService;
import com.lckback.lckforall.base.setting.SwaggerPageable;
import com.lckback.lckforall.community.dto.PostDto;
import com.lckback.lckforall.community.service.PostService;
import jakarta.validation.Valid;
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
    @SwaggerPageable
    public ResponseEntity<ApiResponse<PostDto.PostListResponse>> getPosts(Pageable pageable, @RequestParam String postType) {

        PostDto.PostListResponse data = postService.findPosts(pageable, postType);

        return ResponseEntity.ok()
                .body(ApiResponse.createSuccess(data));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Void>> createPost(
            @RequestPart(required = false) List<MultipartFile> files,
            @RequestPart @Valid PostDto.CreatePostRequest request,
            @RequestHeader("Authorization") String token) {
        String kakaoUserId = authService.getKakaoUserId(token);
        postService.createPost(files, request, kakaoUserId);

        return ResponseEntity.ok()
                .body(ApiResponse.createSuccessWithNoContent());
    }

    @GetMapping("/type-list")
    public ResponseEntity<ApiResponse<PostDto.PostTypeListResponse>> getPostTypes(
            @RequestHeader("Authorization") String token) {
        PostDto.PostTypeListResponse response = postService.getPostTypes();

        return ResponseEntity.ok()
                .body(ApiResponse.createSuccess(response));
    }


    @GetMapping("/{postId}/detail")
    public ResponseEntity<ApiResponse<PostDto.PostDetailResponse>> getPostDetail(
            @RequestHeader("Authorization") String token,
            //title, content, 파일, iD 응원팀, 댓글 날짜 -> dto 만들어서
            @PathVariable Long postId) {
        PostDto.PostDetailResponse response = postService.getPostDetail(postId);

        return ResponseEntity.ok()
                .body(ApiResponse.createSuccess(response));
    }


    //post 삭제하기
    @DeleteMapping("/{postId}/delete")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @RequestHeader("Authorization") String token,
            @PathVariable Long postId)
    {
        String kakaoUserId = authService.getKakaoUserId(token);
        postService.deletePost(postId, kakaoUserId);

        return ResponseEntity.ok()
                .body(ApiResponse.createSuccessWithNoContent());
    }

    //post 수정
    @PatchMapping("/{postId}/modify")
    public ResponseEntity<ApiResponse<Void>> modifyPost(
            @RequestHeader("Authorization") String token,
            @PathVariable Long postId,
            @RequestBody PostDto.PostModifyRequest request,
            @RequestPart(required = false) List<MultipartFile> files
    ) {
        String kakaoUserId = authService.getKakaoUserId(token);
        postService.updatePost(files, request, postId, kakaoUserId);

        return ResponseEntity.ok()
                .body(ApiResponse.createSuccessWithNoContent());
    }
}
