package com.lckback.lckforall.community.dto;

import com.lckback.lckforall.community.model.PostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public class PostDto {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PostListResponse{
        List<PostDetail> postDetailList;
        Boolean isLast;
    }

    @Getter
    @Builder
    public static class PostDetail {
        private Long postId;
        private String postTitle;
        private LocalDate postCreatedAt;
        private String userNickname;
        private String supportTeamName;
        private String postPicture;
        private Integer commentCounts;
    }

    @Getter
    @Builder
    public static class CreatePostRequest {
        //게시판 타입, 제목, 내용, 사진 영상 및 업로드
        private PostType postType;
        private String postTitle;
        private String postContent;

    }

    @Getter
    @Builder
    public class PostTypeListResponse {
        List<String> postTypeList;
    }
}
