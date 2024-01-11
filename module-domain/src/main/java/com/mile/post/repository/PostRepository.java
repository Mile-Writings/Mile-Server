package com.mile.post.repository;

import com.mile.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    boolean existsPostByIdAndWriterNameId(final Long postId, final Long userId);
}
