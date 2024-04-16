package com.mile.post.repository;

import com.mile.post.domain.Post;
import com.mile.topic.domain.Topic;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    boolean existsPostByIdAndWriterNameId(final Long postId, final Long userId);
    List<Post> findByTopic(final Topic topic);
    int countByWriterNameId(final Long writerNameId);
}
