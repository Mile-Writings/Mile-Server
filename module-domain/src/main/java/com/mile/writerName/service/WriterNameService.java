package com.mile.writerName.service;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;
import com.mile.writerName.domain.WriterName;
import com.mile.writerName.repository.WriterNameRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class WriterNameService {
    private final WriterNameRepository writerNameRepository;

    public boolean isUserInMoim(
            final Long moimId,
            final Long writerId
    ) {
        return writerNameRepository.findByMoimIdAndWriterId(moimId, writerId).isPresent();
    }

    public WriterName findByMoimAndUser(
            final Long moimId,
            final Long writerId
    ) {
        return writerNameRepository.findByMoimIdAndWriterId(moimId, writerId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.USER_AUTHENTICATE_ERROR)
                );
    }

    public int findNumbersOfWritersByMoimId(
            final Long moimId
    ) {
        return writerNameRepository.findByMoimId(moimId).size();
    }

    public String getOwnerNameOfMoimId(
            final Long moimId
    ) {
        return writerNameRepository.getOwnerWriterNameByMoimId(moimId);
    }

    public List<WriterName> findWriterNamesByMoimId(
            final Long moimId
    ) {
        return writerNameRepository.findByMoimId(moimId);
    }

    public WriterName findByWriterId(
            final Long writerId
    ) {
        return writerNameRepository.findByWriterId(writerId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.WRITER_NOT_FOUND)
                );
    }

    public void decreaseTotalCuriousCountByWriterId(
            final Long writerId
    ) {
        WriterName writerName = findByWriterId(writerId);
        writerName.decreaseTotalCuriousCount();
    }

    public void increaseTotalCuriousCountByWriterId(
            final Long writerId
    ) {
        WriterName writerName = findByWriterId(writerId);
        writerName.increaseTotalCuriousCount();
    }

    public List<WriterName> findTop2ByCuriousCount(final Long moimid) {
        return writerNameRepository.findTop2ByMoimIdOrderByTotalCuriousCountDesc(moimid);
    }
}