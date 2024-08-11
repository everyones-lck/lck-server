package com.lckback.lckforall.community.service;

import com.lckback.lckforall.base.api.error.PostErrorCode;
import com.lckback.lckforall.base.api.error.UserErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;
import com.lckback.lckforall.community.dto.PostDto;
import com.lckback.lckforall.community.model.Post;
import com.lckback.lckforall.community.model.PostFile;
import com.lckback.lckforall.community.model.PostType;
import com.lckback.lckforall.community.repository.PostFileRepository;
import com.lckback.lckforall.community.repository.PostRepository;
import com.lckback.lckforall.community.repository.PostTypeRepository;
import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.user.respository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final UserRepository userRepository;

    public PostDto.PostListResponse findPosts(Pageable pageable, String postType) {
        PostType foundPostType = postTypeRepository.findByType(postType).
                orElseThrow(() -> new RestApiException(PostErrorCode.POST_NOT_FOUND));

        Page<Post> posts = postRepository.findAllByPostType(pageable, foundPostType);
        List<PostDto.PostDetail> list = posts.stream().map(post ->  PostDto.PostDetail.builder()
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

        //title content postType userx
        Post post = Post.builder()
                .user(user)
                .title(request.getPostTitle())
                .content(request.getPostContent())
                .postType(request.getPostType())
                .build();

        postRepository.save(post);

        List<PostFile> postFiles = new ArrayList<>();
        //controller에서 받은 파일을 업로드를 하고 그 url을 가져와야함.
        for (MultipartFile file : files) {
            String fileUrl = s3Service.upload(); //url이 담겨있음
            PostFile postFile = PostFile.builder()
                    .url(fileUrl)
                    .post(post)
                    .build();
            postFileRepository.save(postFile);
        }
        //그리고 request에서 포스트와 합쳐.
    }

    public PostDto.PostTypeListResponse getPostTypes() {

        List<PostType> postTypes = postTypeRepository.findAll();
        List<String> postTypeList = postTypes.stream().map(PostType::getType).toList();
        return PostDto.PostTypeListResponse.builder().postTypeList(postTypeList).build();
    }
}
