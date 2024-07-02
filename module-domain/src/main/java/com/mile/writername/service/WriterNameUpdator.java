package com.mile.writername.service;

import com.mile.writername.domain.WriterName;
import com.mile.writername.repository.WriterNameRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class WriterNameUpdator {

    private final WriterNameRepository writerNameRepository;

    public void decreaseTotalCuriousCountByWriterName(
            final WriterName writerName
    ) {
        writerName.decreaseTotalCuriousCount();
        writerNameRepository.save(writerName);
    }

    public void increaseTotalCuriousCountByWriterName(
            final WriterName writerName
    ) {
        writerName.increaseTotalCuriousCount();
        writerNameRepository.save(writerName);
    }
}
