package com.mile.comment.service.dto;

import com.mile.comment.domain.Comment;
import com.mile.writername.domain.WriterName;

public record CommentResponse(
        String commentId,
        String name,
        String moimName,
        String content,
        boolean isMyComment
) {
    private final static String ANONYMOUS = "작자미상";
    private final static String AUTHOR = "글쓴이";

    public static CommentResponse of(
            final Comment comment,
            final Long writerNameId,
            final boolean isWriterOfPost
    ) {
        WriterName writerName = comment.getWriterName();
        String name = ANONYMOUS + writerName.getId().toString();
        if (isWriterOfPost) {
            name = AUTHOR;
        }
        return new CommentResponse(
                comment.getIdUrl(),
                name,
                writerName.getMoim().getName(),
                comment.getContent(),
                writerName.getId().equals(writerNameId)
        );
    }
}
