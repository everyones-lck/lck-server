package com.lckback.lckforall.community.converter.post;

import java.util.List;

import com.lckback.lckforall.community.dto.post.GetPostDetailDto;
import com.lckback.lckforall.community.model.Comment;
import com.lckback.lckforall.community.model.Post;
import com.lckback.lckforall.community.model.PostFile;

public class GetPostDetailConverter {

	public static GetPostDetailDto.Parameter convertParameter(Long postId) {
		return GetPostDetailDto.Parameter.builder()
			.postId(postId)
			.build();
	}

	public static GetPostDetailDto.Response convertResponse(Post post, List<PostFile> postFiles,
		List<Comment> comments) {
		List<GetPostDetailDto.FileDetail> fileList = postFiles
			.stream()
			.map(GetPostDetailConverter::convertFileDetail)
			.toList();

		List<GetPostDetailDto.CommentDetail> commentList = comments
			.stream()
			.map(GetPostDetailConverter::convertCommentDetail)
			.toList();

		return GetPostDetailDto.Response.builder()
			.postType(post.getPostType().getType())
			.writerProfileUrl(post.getUser().getProfileImageUrl())
			.writerNickname(post.getUser().getNickname())
			.writerTeam(post.getUser().getTeam().getTeamName())
			.postTitle(post.getTitle())
			.postCreatedAt(post.getCreatedAt())
			.content(post.getContent())
			.fileList(fileList)
			.commentList(commentList)
			.build();
	}

	public static GetPostDetailDto.FileDetail convertFileDetail(PostFile postFile) {
		return GetPostDetailDto.FileDetail.builder()
			.fileUrl(postFile.getUrl())
			.isImage(postFile.getIsImage())
			.build();
	}

	public static GetPostDetailDto.CommentDetail convertCommentDetail(Comment comment) {
		return GetPostDetailDto.CommentDetail.builder()
			.profileImageUrl(comment.getUser().getProfileImageUrl())
			.nickname(comment.getUser().getNickname())
			.supportTeam(comment.getUser().getTeam().getTeamName())
			.content(comment.getContent())
			.createdAt(comment.getCreatedAt())
			.commentId(comment.getId())
			.build();
	}
}
