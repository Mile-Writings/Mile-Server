package com.mile.commentreply.repository;

import com.mile.comment.domain.Comment;
import com.mile.commentreply.domain.CommentReply;
import com.mile.post.domain.Post;
import com.mile.writername.domain.WriterName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentReplyRepository extends JpaRepository<CommentReply, Long> {

    List<CommentReply> findByComment(final Comment comment);


    @Query("select count(commentReply) from CommentReply commentReply where commentReply.writerName = :writerName")
    int countByWriterName(@Param("writerName") final WriterName writerName);

    @Query("select count(commentReply) from CommentReply commentReply where commentReply.comment.post = :post")
    int countByPost(final Post post);

    @Transactional
    @Modifying
    @Query("DELETE FROM CommentReply c WHERE c.comment = :comment")
    void deleteCommentRepliesByComment(final Comment comment);

    @Transactional
    @Modifying
    @Query("DELETE FROM CommentReply c WHERE c.writerName = :writerName")
    void deleteCommentRepliesByWriterName(@Param("writerName") final WriterName writerName);
}
