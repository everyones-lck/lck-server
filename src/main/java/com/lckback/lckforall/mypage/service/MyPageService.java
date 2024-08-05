package com.lckback.lckforall.mypage.service;

import com.lckback.lckforall.base.api.error.CommonErrorCode;
import com.lckback.lckforall.base.api.error.TeamErrorCode;
import com.lckback.lckforall.base.api.error.UserErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;
import com.lckback.lckforall.community.model.Comment;
import com.lckback.lckforall.community.model.Post;
import com.lckback.lckforall.community.repository.CommentRepository;
import com.lckback.lckforall.community.repository.PostRepository;
import com.lckback.lckforall.mypage.dto.GetUserCommentDto;
import com.lckback.lckforall.mypage.dto.GetUserPostDto;
import com.lckback.lckforall.mypage.dto.GetUserProfileDto;
import com.lckback.lckforall.mypage.dto.UpdateMyTeamDto;
import com.lckback.lckforall.mypage.dto.UpdateUserProfileDto;
import com.lckback.lckforall.team.model.Team;
import com.lckback.lckforall.team.repository.TeamRepository;
import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.user.respository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class MyPageService {

	private final UserRepository userRepository;

	private final TeamRepository teamRepository;

	private final PostRepository postRepository;

	private final CommentRepository commentRepository;

	public GetUserProfileDto.Response getUserProfile(Long userId) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));

		return GetUserProfileDto.Response.builder()
			.nickname(user.getNickname())
			.profileImageUrl(user.getProfileImageUrl())
			.teamLogoUrl(user.getTeam().getTeamLogoUrl())
			.tier("temp")
			.build();
	}

	public void withdrawFromAccount(Long userId) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));

		user.withdrawFromAccount();
	}

	public UpdateUserProfileDto.Response updateUserProfile(
		Long userId,
		MultipartFile profileImage,
		UpdateUserProfileDto.Request request) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));

		if (request.getNickname() != null) {

			if (userRepository.existsByNickname(request.getNickname())) {
				throw new RestApiException(UserErrorCode.ALREADY_EXIST_NICKNAME);
			}

			user.updateNickname(request.getNickname());
		}

		if (request.isDefaultImage() && profileImage.isEmpty()) {
			user.updateProfileImageUrl("defaultImage");
		}

		if (!request.isDefaultImage() && !profileImage.isEmpty()) {
			String updatedProfileImageUrl = "temp"; // s3 도입 전까지 임시
			user.updateProfileImageUrl(updatedProfileImageUrl);
		}

		return UpdateUserProfileDto.Response.builder()
			.updatedNickname(user.getNickname())
			.updatedProfileImageUrl(user.getProfileImageUrl())
			.build();
	}

	public void updateMyTeam(Long userId, UpdateMyTeamDto.Request request) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));

		Team team = teamRepository.findById(request.getTeamId())
			.orElseThrow(() -> new RestApiException(TeamErrorCode.NOT_EXISTS_TEAM));

		LocalDateTime oneMonthAgo = LocalDateTime.now().minus(1, ChronoUnit.MONTHS);

		if (user.getLastUpdatedMyTeam().isAfter(oneMonthAgo)) {
			throw new RestApiException(TeamErrorCode.MY_TEAM_CANNOT_UPDATE);
		}

		user.updateMyTeam(team);
	}

	public GetUserPostDto.Response getUserPost(Long userId, Pageable pageable) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));

		Page<Post> posts = postRepository.findByUser(user, pageable);

		return GetUserPostDto.Response.builder()
			.posts(convertToPostInformation(posts))
			.isLast(posts.isLast())
			.build();
	}

	public GetUserCommentDto.Response getUserComment(Long userId, Pageable pageable) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));

		Page<Comment> comments = commentRepository.findByUser(user, pageable);

		return GetUserCommentDto.Response.builder()
			.comments(convertToCommentInformation(comments))
			.isLast(comments.isLast())
			.build();
	}

	private List<GetUserPostDto.Information> convertToPostInformation(Page<Post> posts) {
		return posts.stream()
			.map(post -> GetUserPostDto.Information.builder()
				.id(post.getId())
				.title(post.getTitle())
				.postType(post.getPostType().getType())
				.build())
			.collect(Collectors.toList());
	}

	private List<GetUserCommentDto.Information> convertToCommentInformation(
		Page<Comment> comments) {
		return comments.stream()
			.map(comment -> GetUserCommentDto.Information.builder()
				.id(comment.getId())
				.content(comment.getContent())
				.postType(comment.getPost().getPostType().getType())
				.build())
			.collect(Collectors.toList());
	}

}
