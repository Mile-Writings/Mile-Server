package com.mile.post.repository;

import com.mile.post.domain.Post;
import com.mile.topic.domain.Topic;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    boolean existsPostByIdAndWriterNameId(final Long postId, final Long userId);
    List<Post> findByTopic(final Topic topic);
    List<Post> findByWriterNameId(final Long writerNameId);
    int countByWriterNameId(final Long writerNameId);


    @Modifying
    @Query("DELETE FROM Post p WHERE p.topic = :topic")
    void deleteByTopic(@Param("topic") Topic topic);
}
