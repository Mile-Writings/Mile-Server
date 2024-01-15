package com.mile.writerName.service.dto;

public record WriterNameResponse(
        String postId,
        String writerName
) {
    public static WriterNameResponse of(
            final String postId,
            final String writerName
    ) {
        return new WriterNameResponse(postId, writerName);
    }
}
