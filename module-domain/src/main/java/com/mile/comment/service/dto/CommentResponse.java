package com.mile.comment.service.dto;

import com.mile.comment.domain.Comment;
import com.mile.writerName.domain.WriterName;

public record CommentResponse(
    Long commentId,
    String name,
    String moimName,
    String content,
    boolean isMyComment
) {
    private final static String ANONYMOUS = "작자미상";

    public static CommentResponse of(
            final Comment comment,
            final Long userId
    ) {
        WriterName writerName = comment.getWriterName();
        return new CommentResponse(
                comment.getId(),
                ANONYMOUS + writerName.getId().toString(),
                writerName.getMoim().getName(),
                comment.getContent(),
                writerName.getId().equals(userId)
                );
    }
}
