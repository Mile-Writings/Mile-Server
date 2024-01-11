package com.mile.writerName.service.dto;
import com.mile.writerName.domain.WriterName;

import java.util.List;
import java.util.stream.Collectors;

public record PopularWriterListResponse(List<PopularWriterResponse> popularWriters) {

    public static PopularWriterListResponse of(final List<WriterName> writers) {
        return new PopularWriterListResponse(
                writers
                .stream()
                .map(PopularWriterResponse::of)
                .collect(Collectors.toList()));
    }

}