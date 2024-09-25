package com.lckback.lckforall.community.repository;

import java.util.Optional;

import com.lckback.lckforall.community.model.Post;
import com.lckback.lckforall.community.model.PostType;
import com.lckback.lckforall.user.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
	@EntityGraph(attributePaths = {"user", "comments"})
	Page<Post> findAllByPostType(Pageable pageable, PostType postType);

	Page<Post> findByUser(User user, Pageable pageable);

	@Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.id = :postId")
	Optional<Post> findByIdWithUser(Long postId);

	@Query("SELECT p FROM Post p JOIN FETCH p.postFiles WHERE p.id = :postId")
	Optional<Post> findByIdWithPostFiles(Long postId);
}
