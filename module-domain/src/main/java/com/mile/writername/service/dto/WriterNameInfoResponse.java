package com.mile.writername.service.dto;

public record WriterNameInfoResponse(
        Long writerNameId,
        String writerName,
        int postCount,
        int commentCount
) {
    public static WriterNameInfoResponse of(
            final Long writerNameId,
            final String writerName,
            final int postCount,
            final int commentCount
    ) {
        return new WriterNameInfoResponse(writerNameId, writerName, postCount, commentCount);
    }
}
