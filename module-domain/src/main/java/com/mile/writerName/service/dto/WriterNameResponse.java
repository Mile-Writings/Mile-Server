package com.mile.writerName.service.dto;

public record WriterNameResponse(
        Long postId,
        String writerName
) {
    public static WriterNameResponse of(
            final Long postId,
            final String writerName
    ) {
        return new WriterNameResponse(postId, writerName);
    }
}
