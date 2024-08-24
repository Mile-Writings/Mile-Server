package com.mile.writername.service.dto.response;

import com.mile.writername.domain.WriterName;

public record PopularWriterResponse(String writerName, String information) {
    public static PopularWriterResponse of(WriterName writer) {
        return new PopularWriterResponse(writer.getName(), writer.getInformation());
    }
}
