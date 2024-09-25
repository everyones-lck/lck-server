package com.lckback.lckforall.community.converter.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lckback.lckforall.community.dto.post.GetPostListDto;
import com.lckback.lckforall.community.model.Post;

public class GetPostListConverter {

	public static GetPostListDto.Parameter convertParameter(String postType, Pageable pageable) {
		return GetPostListDto.Parameter.builder()
			.pageable(pageable)
			.postType(postType)
			.build();
	}

	public static GetPostListDto.Response convertResponse(Page<Post> posts) {
		return GetPostListDto.Response.builder()
			.postDetailList(posts.stream().map(GetPostListConverter::convertPostDetail).toList())
			.isLast(posts.isLast())
			.build();
	}

	public static GetPostListDto.PostDetail convertPostDetail(Post post) {
		return GetPostListDto.PostDetail.builder()
			.postId(post.getId())
			.postTitle(post.getTitle())
			.postCreatedAt(post.getCreatedAt().toLocalDate())
			.userNickname(post.getUser().getNickname())
			.supportTeamName(post.getUser().getTeam().getTeamName())
			.userProfilePicture(post.getUser().getProfileImageUrl())
			.thumbnailFileUrl(post.getPostFiles().isEmpty() ? "" : post.getPostFiles().get(0).getUrl())
			.commentCounts(post.getComments().size())
			.build();
	}
}
