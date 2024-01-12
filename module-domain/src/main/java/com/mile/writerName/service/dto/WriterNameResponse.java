package com.mile.writerName.service.dto;

public record WriterNameResponse(
        String writerName
) {
    public static WriterNameResponse of(
            final String writerName
    ) {
        return new WriterNameResponse(writerName);
    }
}
