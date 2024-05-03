package com.mile.writername.service.dto;

import com.mile.writername.domain.WriterName;

public record WriterNameDescriptionResponse(
        String name,
        String description

) {

    public static WriterNameDescriptionResponse of(
            final WriterName writerName
    ) {
        return new WriterNameDescriptionResponse(writerName.getName(), writerName.getInformation());
    }
}
