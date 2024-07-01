package com.mile.curious.service;

import com.mile.curious.domain.Curious;
import com.mile.curious.repository.CuriousRepository;
import com.mile.writername.domain.WriterName;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CuriousRetriever {

    private final CuriousRepository curiousRepository;

    public List<Curious> findAllByWriterName(final WriterName writerName) {
        return curiousRepository.findAllByWriterName(writerName);
    }
}
