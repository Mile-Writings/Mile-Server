package com.mile.post.repository;

import com.mile.post.domain.Post;
import com.mile.topic.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    boolean existsPostByIdAndWriterNameId(final Long postId, final Long userId);
    List<Post> findByTopic(final Topic topic);
}
