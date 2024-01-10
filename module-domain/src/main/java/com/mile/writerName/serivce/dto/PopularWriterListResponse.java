package com.mile.writerName.serivce.dto;
import com.mile.writerName.domain.WriterName;
import java.util.ArrayList;
import java.util.List;

public record PopularWriterListResponse(List<PopularWriterResponse> popularWriters) {

    public static PopularWriterListResponse of(final List<WriterName> writers) {
        List<PopularWriterResponse> popularWriters = new ArrayList<>();
        for (WriterName writer : writers) {
            popularWriters.add(PopularWriterResponse.of(writer));
        }
        return new PopularWriterListResponse(popularWriters);
    }

}