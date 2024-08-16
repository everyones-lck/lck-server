package com.lckback.lckforall.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        private String postType;
        private String postTitle;
        private String postContent;

    }

    @Getter
    @Builder
    public static class PostTypeListResponse {
        List<String> postTypeList;
    }


    @Getter
    @Builder
    public static class PostDetailResponse {
        //타입, 작성자 사진(writer profile url),작성자 닉네임, 작성자 응원팀(writer team), 포스트제목, 날짜, 내용, 파일리스트, 댓글리스트
        private String postType;
        private String writerProfileUrl;
        private String writerNickname;
        private String writerTeam;
        private String postTitle;
        private LocalDateTime postCreatedAt;
        private String content;
        private List<String> fileList;
        private List<CommentDto.CommentDetailDto> commentList;

    }

    @Getter
    @Builder
    public static class PostModifyRequest {
        // title content postType
        // postFiles은 따로 받아
        private String postTitle;
        private String postContent;
        private String postType;

    }


}
