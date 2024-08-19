package com.lckback.lckforall.report.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lckback.lckforall.base.api.error.PostErrorCode;
import com.lckback.lckforall.base.api.error.UserErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;
import com.lckback.lckforall.community.model.Post;
import com.lckback.lckforall.community.repository.PostRepository;
import com.lckback.lckforall.report.model.PostReport;
import com.lckback.lckforall.report.repository.PostReportRepository;
import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.user.respository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostReportService {

	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final PostReportRepository postReportRepository;

	@Transactional
	public void createPostReport(String kakaoUserId, Long postId) {
		User user = userRepository.findByKakaoUserId(kakaoUserId)
			.orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new RestApiException(PostErrorCode.POST_NOT_FOUND));

		validateUserCanCreateReport(user, post);

		PostReport report = PostReport.builder()
			.user(user)
			.post(post)
			.build();

		postReportRepository.save(report);
	}

	private void validateUserCanCreateReport(User user, Post post) {
		List<PostReport> postReports = post.getPostReports();

		boolean cannotCreate = postReports.stream()
			.anyMatch(postReport -> postReport.getUser().equals(user));

		if (cannotCreate) {
			throw new RestApiException(PostErrorCode.POST_ALREADY_REPORTED);
		}
	}
}
