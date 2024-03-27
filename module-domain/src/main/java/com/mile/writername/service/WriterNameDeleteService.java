package com.mile.writername.service;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.service.MoimService;
import com.mile.writername.domain.WriterName;
import com.mile.writername.repository.WriterNameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WriterNameDeleteService {

    private final WriterNameRepository writerNameRepository;
    private final MoimService moimService;

    @Transactional
    public void deleteWriterNameById(
            final Long writerNameId,
            final Long userId
    ) {
        WriterName writerName = findById(writerNameId);
        moimService.authenticateOwnerOfMoim(writerName.getMoim(), userId);
        writerNameRepository.delete(writerName);
    }

    private WriterName findById(
            final Long writerNameId
    ) {
        WriterName writerName = writerNameRepository.findById(writerNameId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.WRITER_NOT_FOUND)
        );
        return writerName;
    }
}
