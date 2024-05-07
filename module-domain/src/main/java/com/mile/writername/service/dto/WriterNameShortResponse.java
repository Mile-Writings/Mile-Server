package com.mile.writername.service.dto;

import com.mile.writername.domain.WriterName;

public record WriterNameShortResponse(
        String writerName,
        Long writerNameId
) {
    public static WriterNameShortResponse of(final WriterName writerName) {
        return new WriterNameShortResponse(writerName.getName(), writerName.getId());
    }
}
