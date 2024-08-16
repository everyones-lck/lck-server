package com.lckback.lckforall.community.repository;

import com.lckback.lckforall.community.model.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostFileRepository extends JpaRepository<PostFile, Long> {

    Optional<PostFile> findByUrl(String url);
}
