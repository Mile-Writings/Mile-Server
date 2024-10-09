package com.mile.writername.service.dto.response;

import com.mile.writername.domain.WriterName;

public record WriterNameInformationResponse(
        String writerName,
        Long writerNameId,
        String description
) {
    public static WriterNameInformationResponse of(final WriterName writerName) {
        return new WriterNameInformationResponse(writerName.getName(), writerName.getId(), writerName.getInformation());
    }
}
