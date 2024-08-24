package com.mile.commentreply.service.dto.response;

import com.mile.commentreply.domain.CommentReply;
import com.mile.writername.domain.WriterName;

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
        if (commentReply.isAnonymous()) return ANONYMOUS + writerName.getId().toString();
        if (isWriterOfPost) return AUTHOR;
        return writerName.getName();
    }
}
