package com.mile.comment.repository;

import com.mile.comment.domain.Comment;
import com.mile.post.domain.Post;
import com.mile.writername.domain.WriterName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c.writerName.writer.id from Comment c where c = :comment")
    Long findUserIdByComment(@Param(value = "comment") final Comment comment);

    List<Comment> findByPostId(final Long postId);

    @Modifying(clearAutomatically = true)
    @Query("delete from Comment c where c.post = :post")
    void deleteAllByPost(@Param("post")final Post post);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Comment c where c.writerName = :writerName")
    void deleteAllByWriterName(@Param("writerName") final WriterName writerName);

    int countByPost(final Post post);
    int countByWriterNameId(final Long writerNameId);
}