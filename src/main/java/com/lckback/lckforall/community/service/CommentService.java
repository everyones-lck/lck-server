package com.lckback.lckforall.community.service;

import com.lckback.lckforall.base.api.error.CommentErrorCode;
import com.lckback.lckforall.base.api.error.PostErrorCode;
import com.lckback.lckforall.base.api.error.UserErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;
import com.lckback.lckforall.community.dto.CommentDto;
import com.lckback.lckforall.community.model.Comment;
import com.lckback.lckforall.community.model.Post;
import com.lckback.lckforall.community.repository.CommentRepository;
import com.lckback.lckforall.community.repository.PostRepository;
import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.user.respository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public void createComment(CommentDto.createCommentRequest request, String kakaoUserId) {
        User user = userRepository.findByKakaoUserId(kakaoUserId)
                .orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new RestApiException(PostErrorCode.POST_NOT_FOUND));

        Comment comment = Comment.builder()
                .user(user)
                .content(request.getContent())
                .post(post)
                .build();

        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId, String kakaoUserId) {
        //kakaoUserId와 commentId의 작성자가 일치하는지 확인하고 commentRepository에서 삭제.
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RestApiException(CommentErrorCode.COMMENT_NOT_FOUND));

        validateCommentUser(kakaoUserId, comment);
        commentRepository.delete(comment);
    }

    private static void validateCommentUser(String kakaoUserId, Comment comment) {
        String findKakaoUserId = comment.getUser().getKakaoUserId();

        if (!findKakaoUserId.equals(kakaoUserId)) {
            throw new RestApiException(UserErrorCode.NOT_EXIST_USER);
        }
    }
}
