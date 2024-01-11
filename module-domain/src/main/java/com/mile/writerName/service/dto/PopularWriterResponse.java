package com.mile.writerName.serivce.dto;

import com.mile.writerName.domain.WriterName;

public record PopularWriterResponse(String writerName, String information) {
    public static PopularWriterResponse of(WriterName writer) {
        return new PopularWriterResponse(writer.getName(), writer.getInformation());
    }
}
