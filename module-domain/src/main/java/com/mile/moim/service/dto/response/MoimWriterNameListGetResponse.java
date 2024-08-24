package com.mile.moim.service.dto.response;

import com.mile.writername.service.dto.response.WriterNameInfoResponse;
import java.util.List;

public record MoimWriterNameListGetResponse(
        int pageNumber,
        int writerNameCount,
        List<WriterNameInfoResponse> writerNameList
) {
    public static MoimWriterNameListGetResponse of(
            final int pageNumber,
            final int writerNameCount,
            final List<WriterNameInfoResponse> writerNameList
    ) {
        return new MoimWriterNameListGetResponse(pageNumber, writerNameCount, writerNameList);
    }
}
