package com.lckback.lckforall.community.service;

import com.lckback.lckforall.base.api.error.CommentErrorCode;
import com.lckback.lckforall.base.api.error.PostErrorCode;
import com.lckback.lckforall.base.api.error.UserErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;
import com.lckback.lckforall.community.dto.comment.CreateCommentDto;
import com.lckback.lckforall.community.model.Comment;
import com.lckback.lckforall.community.model.Post;
import com.lckback.lckforall.community.repository.CommentRepository;
import com.lckback.lckforall.community.repository.PostRepository;
import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.user.respository.UserRepository;
import com.lckback.lckforall.community.dto.comment.DeleteCommentDto;

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

	public void createComment(CreateCommentDto.Parameter parameter) {
		User user = userRepository.findByKakaoUserId(parameter.getKakaoUserId())
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));

		Post post = postRepository.findById(parameter.getPostId())
			.orElseThrow(() -> new RestApiException(PostErrorCode.POST_NOT_FOUND));

		Comment comment = Comment.builder()
				.user(user)
				.content(parameter.getContent())
				.post(post)
				.build();

		commentRepository.save(comment);
	}

	public void deleteComment(DeleteCommentDto.Parameter parameter) {
		User user = userRepository.findByKakaoUserIdWithComments(parameter.getKakaoUserId())
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));

		Comment comment = commentRepository.findById(parameter.getCommentId())
			.orElseThrow(() -> new RestApiException(CommentErrorCode.COMMENT_NOT_FOUND));

		//kakaoUserId와 commentId의 작성자가 일치하는지 확인하고 commentRepository에서 삭제.
		validateCommentUser(user, comment);
		commentRepository.delete(comment);
	}

	/**
	 * 댓글 작성자와 삭제하려는 유저가 일치하는지 확인
	 * 추후 댓글 작성자가 비활성 상태일 경우의 로직 추가 필요
	 * @param user 삭제하려는 유저
	 * @param comment 삭제하려는 댓글
	 */
	private void validateCommentUser(User user, Comment comment) {
		if (!user.getComments().contains(comment)) {
			throw new RestApiException(CommentErrorCode.COMMENT_NOT_VALIDATE);
		}
	}
}
