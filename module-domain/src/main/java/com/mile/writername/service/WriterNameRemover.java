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
    private final WriterNameRetriever writerNameRetriever;

    public void deleteWriterNamesByMoim(
            final Moim moim
    ) {
        writerNameRepository.deleteWritersExceptOwner(moim, moim.getOwner());
    }
    public void deleteWriterNameByUserId(
            final Long userId
    ) {
        writerNameRepository.delete(writerNameRetriever.findByWriterId(userId));
    }

    public void deleteWriterName(final WriterName writerName) {
        writerNameRepository.delete(writerName);
    }
    @Transactional
    public void setWriterNameMoimNull(final WriterName writerName) {
        writerName.setMoimNull();
    }
}
