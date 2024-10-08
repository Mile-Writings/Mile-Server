package com.mile.comment.service.dto;

import com.mile.comment.domain.Comment;
import com.mile.commentreply.service.dto.response.ReplyResponse;
import com.mile.writername.domain.WriterName;

import java.util.List;

public record CommentResponse(
        String commentId,
        String name,
        String moimName,
        String content,
        boolean isMyComment,
        boolean isAnonymous,
        List<ReplyResponse> replies
) {
    private final static String ANONYMOUS = "작자미상";
    private final static String AUTHOR = "글쓴이";

    public static CommentResponse of(
            final Comment comment,
            final Long writerNameId,
            final boolean isWriterOfPost,
            final List<ReplyResponse> replies
    ) {
        WriterName writerName = comment.getWriterName();
        return new CommentResponse(
                comment.getIdUrl(),
                getNameString(comment, writerName, isWriterOfPost),
                writerName.getMoim().getName(),
                comment.getContent(),
                writerName.getId().equals(writerNameId),
                comment.isAnonymous(),
                replies
        );
    }

    private static String getNameString(
            final Comment comment,
            final WriterName writerName,
            final boolean isWriterOfPost
    ) {
        if (comment.isAnonymous()) return ANONYMOUS + writerName.getId().toString();
        if (isWriterOfPost) return AUTHOR;
        return writerName.getName();

    }
}
