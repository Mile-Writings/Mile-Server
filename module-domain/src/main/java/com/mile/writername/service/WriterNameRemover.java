package com.mile.writername.service;

import com.mile.moim.domain.Moim;
import com.mile.writername.domain.WriterName;
import com.mile.writername.repository.WriterNameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class WriterNameRemover {

    private final WriterNameRepository writerNameRepository;

    public void deleteWriterNamesByMoim(
            final Moim moim
    ) {
        writerNameRepository.deleteWritersExceptOwner(moim, moim.getOwner());
    }

    @Transactional
    public void setWriterNameMoimNull(final WriterName writerName) {
        writerName.setMoimNull();
    }
}
