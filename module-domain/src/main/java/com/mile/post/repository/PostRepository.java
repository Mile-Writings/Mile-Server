package com.mile.post.repository;

import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import com.mile.topic.domain.Topic;

import java.util.List;

import com.mile.writername.domain.WriterName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    boolean existsPostByIdAndWriterNameId(final Long postId, final Long userId);

    @Query("""
        SELECT p
        FROM Post p
        WHERE p.topic.moim = :moim
    """)
    List<Post> findAllByMoim(final Moim moim);
    List<Post> findByTopic(final Topic topic);

    int countByWriterNameId(final Long writerNameId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Post p WHERE p.topic = :topic")
    void deleteByTopic(@Param("topic") Topic topic);

    @Transactional
    @Modifying
    @Query("DELETE FROM Post  p WHERE p.writerName = :writerName")
    void deleteByWriterName(@Param("writerName") WriterName writerName);
}
