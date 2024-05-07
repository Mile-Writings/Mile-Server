package com.mile.commentreply.repository;

import com.mile.comment.domain.Comment;
import com.mile.commentreply.domain.CommentReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentReplyRepository extends JpaRepository<CommentReply, Long> {

    List<CommentReply> findByComment(final Comment comment);
}
