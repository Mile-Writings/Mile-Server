package com.mile.writername.service;

import com.mile.writername.domain.WriterName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WriterNameUpdator {
    public void decreaseTotalCuriousCountByWriterName(
        final WriterName writerName
) {
    writerName.decreaseTotalCuriousCount();
}

    public void increaseTotalCuriousCountByWriterName(
            final WriterName writerName
    ) {
        writerName.increaseTotalCuriousCount();
    }
}
