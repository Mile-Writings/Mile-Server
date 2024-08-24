package com.mile.writername.service.dto.response;

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
