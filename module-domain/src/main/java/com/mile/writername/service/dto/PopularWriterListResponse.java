package com.mile.writername.service.dto;
import com.mile.writername.domain.WriterName;

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