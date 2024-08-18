package com.lckback.lckforall.community.service;

import com.lckback.lckforall.base.api.error.PostErrorCode;
import com.lckback.lckforall.base.api.error.UserErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;
import com.lckback.lckforall.community.dto.CommentDto;
import com.lckback.lckforall.community.dto.PostDto;
import com.lckback.lckforall.community.model.Comment;
import com.lckback.lckforall.community.model.Post;
import com.lckback.lckforall.community.model.PostFile;
import com.lckback.lckforall.community.model.PostType;
import com.lckback.lckforall.community.repository.PostFileRepository;
import com.lckback.lckforall.community.repository.PostRepository;
import com.lckback.lckforall.community.repository.PostTypeRepository;
import com.lckback.lckforall.s3.service.S3Service;
import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.user.respository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final PostTypeRepository postTypeRepository;
    private final PostFileRepository postFileRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    public PostDto.PostListResponse findPosts(Pageable pageable, String postType) {
        PostType foundPostType = postTypeRepository.findByType(postType)
                        .orElseThrow(() -> new RestApiException(PostErrorCode.POST_NOT_FOUND));

        Page<Post> posts = postRepository.findAllByPostType(pageable, foundPostType);
        List<PostDto.PostDetail> list = posts.stream().map(post -> PostDto.PostDetail.builder()
                .postId(post.getId())
                .postTitle(post.getTitle())
                .postCreatedAt(post.getCreatedAt().toLocalDate())
                .userNickname(post.getUser().getNickname())
                .supportTeamName(post.getUser().getTeam().getTeamName())
                .postPicture(!post.getPostFiles().isEmpty() ? post.getPostFiles().get(0).getUrl() : null) //default url 나오면 업데이트 예정
                .commentCounts(post.getComments().size())
                .build()).toList();

        return PostDto.PostListResponse.builder()
                .postDetailList(list)
                .isLast(posts.isLast())
                .build();
    }

    public void createPost(List<MultipartFile> files, PostDto.CreatePostRequest request, String kakaoUserId) {
        User user = userRepository.findByKakaoUserId(kakaoUserId)
                .orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));

        PostType postType = postTypeRepository.findByType(request.getPostType())
                .orElseThrow(() -> new RestApiException(PostErrorCode.POST_TYPE_NOT_FOUND));

        Post post = Post.builder()
                .user(user)
                .title(request.getPostTitle())
                .content(request.getPostContent())
                .postType(postType)
                .build();

        postRepository.save(post);

        //controller에서 받은 파일을 업로드를 하고 그 url을 가져와야함.
        for (MultipartFile file : files) {
            String fileUrl = s3Service.upload(file); //url이 담겨있음
            PostFile postFile = PostFile.builder()
                    .url(fileUrl)
                    .post(post)
                    .build();
            postFileRepository.save(postFile);
        }
    }

    public PostDto.PostTypeListResponse getPostTypes() {

        List<PostType> postTypes = postTypeRepository.findAll();
        List<String> postTypeList = postTypes.stream().map(PostType::getType).toList();
        return PostDto.PostTypeListResponse.builder().postTypeList(postTypeList).build();
    }

    //PostDetailResponse

    public PostDto.PostDetailResponse getPostDetail(Long postId) {
        Post post = postRepository.findById(postId).
                orElseThrow(() -> new RestApiException(PostErrorCode.POST_NOT_FOUND));

        List<PostFile> postFiles = post.getPostFiles();
        List<String> postFileList = postFiles.stream().map(PostFile::getUrl).toList();

        List<Comment> comments = post.getComments();
        List<CommentDto.CommentDetailDto> commentList = comments.stream().map(
                comment -> new CommentDto.CommentDetailDto(
                        comment.getUser().getProfileImageUrl(),
                        comment.getUser().getNickname(),
                        comment.getUser().getTeam().getTeamLogoUrl(),
                        comment.getContent(),
                        comment.getCreatedAt())
        ).collect(Collectors.toList());


        PostDto.PostDetailResponse postDetailResponse = PostDto.PostDetailResponse.builder()
                .postType(post.getPostType().getType())
                .writerProfileUrl(post.getUser().getProfileImageUrl())
                .writerNickname(post.getUser().getNickname())
                .writerTeam(post.getUser().getTeam().getTeamName())
                .postTitle(post.getTitle())
                .postCreatedAt(post.getCreatedAt())
                .content(post.getContent())
                .fileList(postFileList)
                .commentList(commentList).build();

        return postDetailResponse;
    }


    public void updatePost(List<MultipartFile> files, PostDto.PostModifyRequest request, Long postId, String kakaoUserId) {
        //post 작성자와 kakaoUserId 일치하는지 확인
        User user = userRepository.findByKakaoUserId(kakaoUserId)
                .orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(PostErrorCode.POST_NOT_FOUND));

        validate(user, post);

        String postTypeName = request.getPostType();
        PostType postType = postTypeRepository.findByType(postTypeName)
                .orElseThrow(() -> new RestApiException(PostErrorCode.POST_TYPE_NOT_FOUND));

        //아마존s3 클라우드 서비스. 파일 저장소(멀티파트)
        for (MultipartFile file : files) {
            String fileUrl = s3Service.upload(file);//url이 담겨있음

            PostFile postFile = PostFile.builder()
                    .url(fileUrl)
                    .post(post)
                    .build();
            postFileRepository.save(postFile);
        }

        post.update(request.getPostTitle(),request.getPostContent(),postType); //수정되지 않은 원본 내용도 들어가도록
        postRepository.save(post);
}


public void deletePost(Long postId, String kakaoUserId) {
    User user = userRepository.findByKakaoUserId(kakaoUserId)
            .orElseThrow(() -> new RestApiException(UserErrorCode.NOT_EXIST_USER));

    Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RestApiException(PostErrorCode.POST_NOT_FOUND));

    validate(user, post);
    postRepository.delete(post);
}


private void validate(User user, Post post) {
    if (!user.getPosts().contains(post)) {
        throw new RestApiException(PostErrorCode.POST_NOT_VALIDATE);
    }
}
}


