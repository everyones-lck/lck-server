package com.lckback.lckforall.community.repository;

import com.lckback.lckforall.community.model.PostType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostTypeRepository extends JpaRepository<PostType, Long> {
    Optional<PostType> findByType(String type);
}
