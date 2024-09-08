package com.mile.moim.service.dto.response;
import com.mile.moim.domain.popular.MoimCuriousWriter;
import com.mile.writername.domain.WriterName;

import com.mile.writername.service.dto.response.PopularWriterResponse;
import java.util.List;
import java.util.stream.Collectors;

public record MoimMostCuriousWriterResponse(List<PopularWriterResponse> popularWriters) {

    public static MoimMostCuriousWriterResponse of(final List<MoimCuriousWriter> writers) {
        return new MoimMostCuriousWriterResponse(
                writers
                .stream()
                .map(PopularWriterResponse::of)
                .collect(Collectors.toList()));
    }

}