package com.mile.commentreply.service.dto;

import com.mile.comment.domain.Comment;
import com.mile.comment.service.dto.CommentResponse;
import com.mile.commentreply.domain.CommentReply;
import com.mile.writername.domain.WriterName;

import java.util.List;

public record ReplyResponse(
        String replyId,
        String name,
        String moimName,
        String content,
        boolean isMyReply,
        boolean isAnonymous
) {
    private final static String ANONYMOUS = "작자미상";
    private final static String AUTHOR = "글쓴이";

    public static ReplyResponse of(
            final CommentReply commentReply,
            final Long writerNameId,
            final boolean isWriterOfPost
    ) {
        WriterName writerName = commentReply.getWriterName();
        return new ReplyResponse(
                commentReply.getIdUrl(),
                getNameString(commentReply, writerName, isWriterOfPost),
                writerName.getMoim().getName(),
                commentReply.getContent(),
                writerName.getId().equals(writerNameId),
                commentReply.isAnonymous()
        );
    }

    private static String getNameString(
            final CommentReply commentReply,
            final WriterName writerName,
            final boolean isWriterOfPost
    ) {
        if (isWriterOfPost) {
            return AUTHOR;
        } else if (commentReply.isAnonymous()) {
            return ANONYMOUS + writerName.getId().toString();
        } else {
            return writerName.getName();
        }
    }
}
