package com.mile.commentreply.service;

import com.mile.comment.domain.Comment;
import com.mile.commentreply.domain.CommentReply;
import com.mile.commentreply.service.dto.request.ReplyCreateRequest;
import com.mile.commentreply.service.dto.response.ReplyResponse;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.ForbiddenException;
import com.mile.moim.service.MoimRetriever;
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
    private final MoimRetriever moimRetriever;

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
        if (!commentReplyRetriever.authenticateReplyWithUserId(userId, commentReply) &&
                moimRetriever.isMoimOwnerEqualsUser(commentReply.getWriterName().getMoim(), userId)) {
            throw new ForbiddenException(ErrorMessage.REPLY_USER_FORBIDDEN);
        }

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
