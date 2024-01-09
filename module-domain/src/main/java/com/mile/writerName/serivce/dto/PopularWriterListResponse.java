package com.mile.writerName.serivce.dto;
import com.mile.writerName.domain.WriterName;
import java.util.ArrayList;
import java.util.List;

public record PopularWriterListResponse(List<PopularWriterResponse> popularWriters) {

    public static PopularWriterListResponse of(final WriterName writer1, final WriterName writer2) {
        List<PopularWriterResponse> popularWriters = new ArrayList<>();
        popularWriters.add(PopularWriterResponse.of(writer1));
        popularWriters.add(PopularWriterResponse.of(writer2));
        return new PopularWriterListResponse(popularWriters);
    }

}
