package com.mile.writername.service.dto.response;

import com.mile.moim.domain.popular.MoimCuriousWriter;

public record PopularWriterResponse(String writerName) {
    public static PopularWriterResponse of(MoimCuriousWriter writer) {
        return new PopularWriterResponse(writer.getName());
    }
}
