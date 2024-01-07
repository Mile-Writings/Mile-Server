package com.mile.comment.repository;

import com.mile.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c.user.id from Comment c where c = :comment")
    Long findUserIdByComment(@Param(value = "comment") final Comment comment);

    List<Comment> findByPostId(final Long postId);
}