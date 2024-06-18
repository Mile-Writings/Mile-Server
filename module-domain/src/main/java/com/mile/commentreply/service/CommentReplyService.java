package com.mile.commentreply.service;

import com.mile.comment.domain.Comment;
import com.mile.commentreply.domain.CommentReply;
import com.mile.commentreply.repository.CommentReplyRepository;
import com.mile.commentreply.service.dto.ReplyCreateRequest;
import com.mile.commentreply.service.dto.ReplyResponse;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;
import com.mile.exception.model.UnauthorizedException;
import com.mile.utils.SecureUrlUtil;
import com.mile.writername.domain.WriterName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentReplyService {

    private final CommentReplyRepository commentReplyRepository;
    private final SecureUrlUtil secureUrlUtil;

    @Transactional
    public String createCommentReply(
            final WriterName writerName,
            final Comment comment,
            final ReplyCreateRequest replyCreateRequest
    ) {
        CommentReply commentReply = commentReplyRepository.save(CommentReply.create(writerName, comment, replyCreateRequest.content(), replyCreateRequest.isAnonymous()));
        commentReply.setIdUrl(secureUrlUtil.encodeUrl(commentReply.getId()));
        return commentReply.getId().toString();
    }

    public void deleteCommentReply(
            final Long userId,
            final Long replyId
    ) {
        CommentReply commentReply = findById(replyId);
        authenticateReplyWithUserId(userId, commentReply);
        commentReplyRepository.delete(commentReply);
    }

    private void authenticateReplyWithUserId(
            final Long userId,
            final CommentReply commentReply
    ) {
        if (!commentReply.getWriterName().getWriter().getId().equals(userId)) {
            throw new UnauthorizedException(ErrorMessage.REPLY_USER_FORBIDDEN);
        }
    }

    public void deleteRepliesByComment(
            final Comment comment
    ) {
        commentReplyRepository.deleteCommentRepliesByComment(comment);
    }

    public List<ReplyResponse> findRepliesByComment(
            final Comment comment,
            final Long writerNameId
    ) {
        return commentReplyRepository.findByComment(comment).stream().map(c -> ReplyResponse.of(c, writerNameId, isWriterOfPost(c))).collect(Collectors.toList());
    }

    public int findRepliesCountByComment(
            final Comment comment
    ) {
        return commentReplyRepository.findByComment(comment).size();
    }

    private boolean isWriterOfPost(final CommentReply commentReply) {
        return commentReply.getComment().getPost().getWriterName().getId().equals(commentReply.getWriterName().getId());
    }

    private CommentReply findById(
            final Long replyId
    ) {
        return commentReplyRepository.findById(replyId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.REPLY_NOT_FOUND)
        );
    }

    public void deleteRepliesByComments(final List<Comment> comments) {
        comments.forEach(this::deleteRepliesByComment);
    }
}
