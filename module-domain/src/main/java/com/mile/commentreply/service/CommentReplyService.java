package com.mile.commentreply.service;

import com.mile.comment.domain.Comment;
import com.mile.commentreply.domain.CommentReply;
import com.mile.commentreply.service.dto.ReplyCreateRequest;
import com.mile.commentreply.service.dto.ReplyResponse;
import com.mile.writername.domain.WriterName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentReplyService {

    private final CommentReplyRetriever commentReplyRetriever;
    private final CommentReplyCreator commentReplyCreator;
    private final CommentReplyRemover commentReplyRemover;

    @Transactional
    public String createCommentReply(
            final WriterName writerName,
            final Comment comment,
            final ReplyCreateRequest replyCreateRequest
    ) {
        return commentReplyCreator.createCommentReply(writerName, comment, replyCreateRequest);
    }

    public void deleteCommentReply(
            final Long userId,
            final Long replyId
    ) {
        CommentReply commentReply = commentReplyRetriever.findById(replyId);
        commentReplyRetriever.authenticateReplyWithUserId(userId, commentReply);
        commentReplyRemover.deleteCommentReply(commentReply);
    }

    public List<ReplyResponse> findRepliesByComment(
            final Comment comment,
            final Long writerNameId
    ) {
        return commentReplyRetriever.findRepliesByComment(comment, writerNameId);
    }

    public int findRepliesCountByComment(
            final Comment comment
    ) {
        return commentReplyRetriever.findRepliesCountByComment(comment);
    }

}
