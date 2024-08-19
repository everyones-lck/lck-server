package com.lckback.lckforall.report.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lckback.lckforall.base.api.error.CommentErrorCode;
import com.lckback.lckforall.base.api.error.UserErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;
import com.lckback.lckforall.community.model.Comment;
import com.lckback.lckforall.community.repository.CommentRepository;
import com.lckback.lckforall.report.model.CommentReport;
import com.lckback.lckforall.report.repository.CommentReportRepository;
import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.user.respository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentReportService {

	private final UserRepository userRepository;
	private final CommentRepository commentRepository;
	private final CommentReportRepository commentReportRepository;

	@Transactional
	public void createCommentReport(String kakaoUserId, Long commentId) {
		User user = userRepository.findByKakaoUserId(kakaoUserId)
			.orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new RestApiException(CommentErrorCode.COMMENT_NOT_FOUND));

		validateUserCanCreateReport(user, comment);

		CommentReport report = CommentReport.builder()
			.user(user)
			.comment(comment)
			.build();

		commentReportRepository.save(report);
	}

	private void validateUserCanCreateReport(User user, Comment comment) {
		List<CommentReport> commentReportList = comment.getCommentReports();

		boolean cannotCreate = commentReportList.stream()
			.anyMatch(commentReport -> commentReport.getUser().equals(user));

		if (cannotCreate) {
			throw new RestApiException(CommentErrorCode.COMMENT_ALREADY_REPORTED);
		}
	}
}
