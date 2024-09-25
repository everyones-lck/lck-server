package com.lckback.lckforall.community.service;

import com.lckback.lckforall.base.api.error.PostErrorCode;
import com.lckback.lckforall.base.api.error.UserErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;
import com.lckback.lckforall.community.converter.post.GetPostDetailConverter;
import com.lckback.lckforall.community.converter.post.GetPostListConverter;
import com.lckback.lckforall.community.converter.post.GetPostTypeListConverter;
import com.lckback.lckforall.community.converter.post.ModifyPostConverter;
import com.lckback.lckforall.community.dto.post.CreatePostDto;
import com.lckback.lckforall.community.dto.post.DeletePostDto;
import com.lckback.lckforall.community.dto.post.GetPostDetailDto;
import com.lckback.lckforall.community.dto.post.GetPostListDto;
import com.lckback.lckforall.community.dto.post.GetPostTypeListDto;
import com.lckback.lckforall.community.dto.post.ModifyPostDto;
import com.lckback.lckforall.community.model.Comment;
import com.lckback.lckforall.community.model.Post;
import com.lckback.lckforall.community.model.PostFile;
import com.lckback.lckforall.community.model.PostType;
import com.lckback.lckforall.community.repository.CommentRepository;
import com.lckback.lckforall.community.repository.PostFileRepository;
import com.lckback.lckforall.community.repository.PostRepository;
import com.lckback.lckforall.community.repository.PostTypeRepository;
import com.lckback.lckforall.s3.service.S3Service;
import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.user.respository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class PostService {

	private final PostRepository postRepository;
	private final PostTypeRepository postTypeRepository;
	private final PostFileRepository postFileRepository;
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final S3Service s3Service;

	public GetPostListDto.Response findPosts(GetPostListDto.Parameter parameter) {
		PostType foundPostType = postTypeRepository.findByType(parameter.getPostType())
			.orElseThrow(() -> new RestApiException(PostErrorCode.POST_NOT_FOUND));

		PageRequest pageRequest = PageRequest.of(parameter.getPageable().getPageNumber(),
			parameter.getPageable().getPageSize(),
			Sort.by("createdAt").descending());

		Page<Post> posts = postRepository.findAllByPostType(pageRequest, foundPostType);

		return GetPostListConverter.convertResponse(posts);
	}

	public CreatePostDto.Response createPost(CreatePostDto.Parameter parameter) {
		User user = userRepository.findByKakaoUserId(parameter.getKakaoUserId())
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));

		PostType postType = postTypeRepository.findByType(parameter.getPostType())
			.orElseThrow(() -> new RestApiException(PostErrorCode.POST_TYPE_NOT_FOUND));

		Post post = Post.builder()
			.user(user)
			.title(parameter.getPostTitle())
			.content(parameter.getPostContent())
			.postType(postType)
			.build();

		postRepository.save(post);

		uploadPostFiles(post, parameter.getFiles());

		return CreatePostDto.Response.builder()
			.postId(post.getId())
			.build();
	}

	public GetPostTypeListDto.Response getPostTypes() {
		List<PostType> postTypes = postTypeRepository.findAll();
		if (postTypes.isEmpty()) {
			throw new RestApiException(PostErrorCode.POST_TYPE_NOT_FOUND);
		}
		return GetPostTypeListConverter.convertResponse(postTypes);
	}

	//PostDetailResponse
	public GetPostDetailDto.Response getPostDetail(GetPostDetailDto.Parameter parameter) {
		Post post = postRepository.findByIdWithUser(parameter.getPostId()).
			orElseThrow(() -> new RestApiException(PostErrorCode.POST_NOT_FOUND));

		List<PostFile> postFiles = postFileRepository.findByPost(post);

		List<Comment> comments = commentRepository.findByPost(post);

		return GetPostDetailConverter.convertResponse(post, postFiles, comments);
	}

	public ModifyPostDto.Response updatePost(ModifyPostDto.Parameter parameter) {
		User user = userRepository.findByKakaoUserId(parameter.getKakaoUserId())
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));

		Post post = postRepository.findById(parameter.getPostId())
			.orElseThrow(() -> new RestApiException(PostErrorCode.POST_NOT_FOUND));

		validateUserContainsPost(user, post);

		PostType postType = postTypeRepository.findByType(parameter.getPostType())
			.orElseThrow(() -> new RestApiException(PostErrorCode.POST_TYPE_NOT_FOUND));

		post.update(parameter.getPostTitle(), parameter.getPostContent(), postType);
		postRepository.save(post);

		return ModifyPostConverter.convertResponse(post.getId());
	}

	public void deletePost(DeletePostDto.Parameter parameter) {
		User user = userRepository.findByKakaoUserId(parameter.getKakaoUserId())
			.orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));

		Post post = postRepository.findByIdWithPostFiles(parameter.getPostId())
			.orElseThrow(() -> new RestApiException(PostErrorCode.POST_NOT_FOUND));

		validateUserContainsPost(user, post);

		removePostFiles(post.getPostFiles());

		postRepository.delete(post);
	}

	private void removePostFiles(List<PostFile> postFiles) {
		postFiles.forEach(postFile -> s3Service.deleteImageFromS3(postFile.getUrl()));
	}

	/**
	 * 게시글 작성자와 게시글이 일치하는지 확인하는 메서드
	 * @param user 작성자
	 * @param post 게시글
	 */
	private void validateUserContainsPost(User user, Post post) {
		if (!user.getPosts().contains(post)) {
			throw new RestApiException(PostErrorCode.POST_NOT_VALIDATE);
		}
	}

	/**
	 * 게시글 파일 업로드 메서드
	 * 파일 업로드시 예외처리 추가
	 * 	- 파일이 비어있는 경우 앞선 파일들을 삭제한 후 exception 호출
	 * @param post 게시글
	 * @param files 파일 리스트
	 */
	private void uploadPostFiles(Post post, List<MultipartFile> files) {
		if (files == null || files.isEmpty()) {
			return;
		}

		List<String> uploadedFileUrls = new ArrayList<>();

		// 파일이 비어있는 경우 앞선 파일들을 삭제한 후 exception 호출
		for (MultipartFile file : files) {
			if (file.isEmpty()) {
				uploadedFileUrls.forEach(s3Service::deleteImageFromS3);
				throw new RestApiException(PostErrorCode.FILE_IS_EMPTY);
			}
			String fileUrl = s3Service.upload(file);

			// 파일 업로드 성공시 파일 url을 리스트에 추가
			uploadedFileUrls.add(fileUrl);

			PostFile postFile = PostFile.builder()
				.url(fileUrl)
				.post(post)
				.isImage(s3Service.isImage(file))
				.build();
			postFileRepository.save(postFile);
		}
	}
}


