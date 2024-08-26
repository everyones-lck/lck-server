package com.lckback.lckforall.mypage.service;

import com.lckback.lckforall.base.api.error.CommonErrorCode;
import com.lckback.lckforall.base.api.error.TeamErrorCode;
import com.lckback.lckforall.base.api.error.TokenErrorCode;
import com.lckback.lckforall.base.api.error.UserErrorCode;
import com.lckback.lckforall.base.api.error.ViewingPartyErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;
import com.lckback.lckforall.base.auth.jwt.model.RefreshToken;
import com.lckback.lckforall.base.auth.jwt.repository.RefreshTokenRepository;
import com.lckback.lckforall.community.model.Comment;
import com.lckback.lckforall.community.model.Post;
import com.lckback.lckforall.community.repository.CommentRepository;
import com.lckback.lckforall.community.repository.PostRepository;
import com.lckback.lckforall.mypage.dto.DeleteParticipationDto;
import com.lckback.lckforall.mypage.dto.DeleteViewingPartyDto;
import com.lckback.lckforall.mypage.dto.GetUserCommentDto;
import com.lckback.lckforall.mypage.dto.GetUserPostDto;
import com.lckback.lckforall.mypage.dto.GetUserProfileDto;
import com.lckback.lckforall.mypage.dto.GetViewingPartyDto;
import com.lckback.lckforall.mypage.dto.UpdateMyTeamDto;
import com.lckback.lckforall.mypage.dto.UpdateUserProfileDto;
import com.lckback.lckforall.s3.service.S3Service;
import com.lckback.lckforall.team.model.Team;
import com.lckback.lckforall.team.repository.TeamRepository;
import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.user.respository.UserRepository;
import com.lckback.lckforall.viewing.model.Participate;
import com.lckback.lckforall.viewing.model.ViewingParty;
import com.lckback.lckforall.viewing.repository.ParticipateRepository;
import com.lckback.lckforall.viewing.repository.ViewingPartyRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class MyPageService {

	@Value("${default.image.url}")
	private String defaultImageUrl;
	private final UserRepository userRepository;

	private final TeamRepository teamRepository;

	private final PostRepository postRepository;

	private final CommentRepository commentRepository;

	private final ParticipateRepository participateRepository;

	private final ViewingPartyRepository viewingPartyRepository;

	private final RefreshTokenRepository refreshTokenRepository;

	private final S3Service s3Service;

	public GetUserProfileDto.Response getUserProfile(String kakaoUserId) {

		User user = userRepository.findByKakaoUserId(kakaoUserId)
			.orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));

		return GetUserProfileDto.Response.builder()
			.nickname(user.getNickname())
			.profileImageUrl(user.getProfileImageUrl())
			.teamId(user.getTeam().getId())
			.tier("bronze")
			.build();
	}

	public void withdrawFromAccount(String kakaoUserId) {

		User user = userRepository.findByKakaoUserId(kakaoUserId)
			.orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));

		userRepository.delete(user);
	}

	public UpdateUserProfileDto.Response updateUserProfile(
		String kakaoUserId,
		MultipartFile profileImage,
		UpdateUserProfileDto.Request request) {

		User user = userRepository.findByKakaoUserId(kakaoUserId)
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));

		if (request.getNickname() != null) {

			if (userRepository.existsByNickname(request.getNickname())) {
				throw new RestApiException(UserErrorCode.ALREADY_EXIST_NICKNAME);
			}

			user.updateNickname(request.getNickname());
		}

		if (request.isDefaultImage() && profileImage.isEmpty()) {
			user.updateProfileImageUrl(defaultImageUrl);
		}

		if (!request.isDefaultImage() && !profileImage.isEmpty()) {
			String updatedProfileImageUrl = s3Service.upload(profileImage);
			user.updateProfileImageUrl(updatedProfileImageUrl);
		}

		return UpdateUserProfileDto.Response.builder()
			.updatedNickname(user.getNickname())
			.updatedProfileImageUrl(user.getProfileImageUrl())
			.build();
	}

	public void updateMyTeam(String kakaoUserId, UpdateMyTeamDto.Request request) {

		User user = userRepository.findByKakaoUserId(kakaoUserId)
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));

		Team team = teamRepository.findById(request.getTeamId())
			.orElseThrow(() -> new RestApiException(TeamErrorCode.NOT_EXISTS_TEAM));

		LocalDateTime oneMonthAgo = LocalDateTime.now().minus(1, ChronoUnit.MONTHS);

		if (user.getLastUpdatedMyTeam().isAfter(oneMonthAgo)) {
			throw new RestApiException(TeamErrorCode.MY_TEAM_CANNOT_UPDATE);
		}

		user.updateMyTeam(team);
	}

	public GetUserPostDto.Response getUserPost(String kakaoUserId, Pageable pageable) {

		User user = userRepository.findByKakaoUserId(kakaoUserId)
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));

		Page<Post> posts = postRepository.findByUser(user, pageable);

		return GetUserPostDto.Response.builder()
			.posts(convertToPostInformation(posts))
			.isLast(posts.isLast())
			.build();
	}

	public GetUserCommentDto.Response getUserComment(String kakaoUserId, Pageable pageable) {

		User user = userRepository.findByKakaoUserId(kakaoUserId)
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));

		Page<Comment> comments = commentRepository.findByUser(user, pageable);

		return GetUserCommentDto.Response.builder()
			.comments(convertToCommentInformation(comments))
			.isLast(comments.isLast())
			.build();
	}

	public GetViewingPartyDto.Response getUserViewingPartyAsParticipate(
		String kakaoUserId,
		Pageable pageable) {

		User user = userRepository.findByKakaoUserId(kakaoUserId)
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));

		Page<Participate> participates = participateRepository.findByUser(user, pageable);

		Page<ViewingParty> viewingParties = participates
			.map(participate -> participate.getViewingParty());

		return GetViewingPartyDto.Response.builder()
			.viewingParties(convertToViewingPartiesInformation(viewingParties))
			.isLast(viewingParties.isLast())
			.build();
	}

	public GetViewingPartyDto.Response getUserViewingPartyAsHost(String kakaoUserId,
		Pageable pageable) {

		User user = userRepository.findByKakaoUserId(kakaoUserId)
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));

		Page<ViewingParty> viewingParties = viewingPartyRepository.findByUser(user, pageable);

		return GetViewingPartyDto.Response.builder()
			.viewingParties(convertToViewingPartiesInformation(viewingParties))
			.isLast(viewingParties.isLast())
			.build();
	}

	public void cancelViewingPartyParticipation(
		String kakaoUserId,
		Long viewingPartyId) {

		User user = userRepository.findByKakaoUserId(kakaoUserId)
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));

		ViewingParty viewingParty = viewingPartyRepository.findById(viewingPartyId)
			.orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));

		Participate participate =
			participateRepository.findByViewingPartyAndUser(viewingParty, user)
				.orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));

		participateRepository.delete(participate);
	}

	public void cancelViewingPartyHosting(
		String kakaoUserId,
		Long viewingPartyId) {

		User user = userRepository.findByKakaoUserId(kakaoUserId)
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));

		ViewingParty viewingParty = viewingPartyRepository.findById(viewingPartyId)
			.orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));

		if (!viewingParty.getUser().equals(user)) {
			throw new RestApiException(ViewingPartyErrorCode.USER_IS_NOT_HOST);
		}

		viewingPartyRepository.delete(viewingParty);
	}

	public void logout(String kakaoUserId, String refreshToken) {

		RefreshToken findRefreshToken = refreshTokenRepository.findById(kakaoUserId)
			.orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));

		if (!refreshToken.equals(findRefreshToken.getRefreshToken())) {
			throw new RestApiException(TokenErrorCode.NOT_EXISTS_REFRESH_TOKEN);
		}

		refreshTokenRepository.delete(findRefreshToken);
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

	private List<GetViewingPartyDto.Information> convertToViewingPartiesInformation(
		Page<ViewingParty> viewingParties) {

		return viewingParties.stream()
			.map(viewingParty -> GetViewingPartyDto.Information.builder()
				.id(viewingParty.getId())
				.name(viewingParty.getName())
				.date(viewingParty.getDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
				.build()
			).collect(Collectors.toList());
	}
}
