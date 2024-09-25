package com.lckback.lckforall.community.repository;

import java.util.List;

import com.lckback.lckforall.community.model.Comment;
import com.lckback.lckforall.community.model.Post;
import com.lckback.lckforall.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByUser(User user, Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    List<Comment> findByPost(Post post);
}
