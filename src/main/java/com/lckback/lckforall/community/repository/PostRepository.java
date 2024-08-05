package com.lckback.lckforall.community.repository;

import com.lckback.lckforall.community.model.Post;
import com.lckback.lckforall.user.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByUser(User user, Pageable pageable);
}
