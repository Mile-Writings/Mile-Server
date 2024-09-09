package com.mile.commentreply.service;

import com.mile.comment.domain.Comment;
import com.mile.commentreply.domain.CommentReply;
import com.mile.commentreply.repository.CommentReplyRepository;
import com.mile.commentreply.service.dto.response.ReplyResponse;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import com.mile.post.domain.Post;

import com.mile.writername.domain.WriterName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentReplyRetriever {

    private final CommentReplyRepository commentReplyRepository;

    public CommentReply findById(
            final Long replyId
    ) {
        return commentReplyRepository.findById(replyId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND)
        );
    }

    private boolean isWriterOfPost(final CommentReply commentReply) {
        return commentReply.getComment().getPost().getWriterName().getId().equals(commentReply.getWriterName().getId());
    }

    public int findRepliesCountByComment(
            final Comment comment
    ) {
        return commentReplyRepository.findByComment(comment).size();
    }

    public List<ReplyResponse> findRepliesByComment(
            final Comment comment,
            final Long writerNameId
    ) {
        return commentReplyRepository.findByComment(comment).stream()
                .map(commentReply -> ReplyResponse.of(commentReply, writerNameId, isWriterOfPost(commentReply)))
                .collect(Collectors.toList());
    }

    public boolean authenticateReplyWithUserId(
            final Long userId,
            final CommentReply commentReply
    ) {
        return commentReply.getWriterName().getWriter().getId().equals(userId);
    }

    public int countByWriterName(final WriterName writerName) {
        return commentReplyRepository.countByWriterName(writerName);
    }

    public int countByPost(final Post post) {
        return commentReplyRepository.countByPost(post);
    }

}
