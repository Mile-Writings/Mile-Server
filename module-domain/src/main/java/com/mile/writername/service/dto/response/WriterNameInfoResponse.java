package com.mile.writername.service.dto.response;

public record WriterNameInfoResponse(
        Long writerNameId,
        String writerName,
        int postCount,
        int commentCount,
        boolean isOwner
) {
    public static WriterNameInfoResponse of(
            final Long writerNameId,
            final String writerName,
            final int postCount,
            final int commentCount,
            final boolean isOwner
    ) {
        return new WriterNameInfoResponse(writerNameId, writerName, postCount, commentCount, isOwner);
    }
}
