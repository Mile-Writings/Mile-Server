package com.mile.commentreply.service;

import com.mile.comment.domain.Comment;
import com.mile.commentreply.domain.CommentReply;
import com.mile.commentreply.repository.CommentReplyRepository;
import com.mile.commentreply.service.dto.ReplyCreateRequest;
import com.mile.utils.SecureUrlUtil;
import com.mile.writername.domain.WriterName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentReplyCreator {

    private final SecureUrlUtil secureUrlUtil;
    private final CommentReplyRepository commentReplyRepository;

    public String createCommentReply(
            final WriterName writerName,
            final Comment comment,
            final ReplyCreateRequest replyCreateRequest
    ) {
        CommentReply commentReply = commentReplyRepository.save(CommentReply.create(writerName, comment, replyCreateRequest.content(), replyCreateRequest.isAnonymous()));
        commentReply.setIdUrl(secureUrlUtil.encodeUrl(commentReply.getId()));
        return commentReply.getId().toString();
    }

}
