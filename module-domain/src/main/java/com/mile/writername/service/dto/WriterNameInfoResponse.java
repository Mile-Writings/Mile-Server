package com.mile.writername.service.dto;

public record WriterNameInfoResponse(
        Long writerNameId,
        String writerName,
        String information
) {
    public static WriterNameInfoResponse of(
            final Long writerNameId,
            final String writerName,
            final String information
    ) {
        return new WriterNameInfoResponse(writerNameId, writerName, information);
    }
}
