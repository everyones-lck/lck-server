package com.lckback.lckforall.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class CommentDto {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class createCommentRequest {
        private String content;
    }

    @Getter
    @AllArgsConstructor
    public static class CommentDetailDto {
        //댓글작성자 프로필사진, 닉네임, 팀, 댓글 내용, 날짜
        private String profileUrl;
        private String nickname;
        private String supportTeam;
        private String content;
        private LocalDateTime createdAt;
        private Long commentId;
    }
}
