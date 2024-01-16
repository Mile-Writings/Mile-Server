package com.mile.writerName.service;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.domain.Moim;
import com.mile.user.domain.User;
import com.mile.writerName.domain.WriterName;
import com.mile.writerName.repository.WriterNameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class WriterNameService {
    private final WriterNameRepository writerNameRepository;
    private final RandomWriterNameService randomWriterNameService;

    public boolean isUserInMoim(
            final Long moimId,
            final Long writerId
    ) {
        return writerNameRepository.findByMoimIdAndWriterId(moimId, writerId).isPresent();
    }

    public Long getWriterNameIdByUserId(
            final Long userId
    ) {
        return findByWriterId(userId).getId();
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
        return getWriterNamesHaveCuriousCount(writerNameRepository.findTop2ByMoimIdOrderByTotalCuriousCountDesc(moimid));
    }

    private List<WriterName> getWriterNamesHaveCuriousCount(
            List<WriterName> writerNameList
    ) {
        writerNameList.removeIf(writerName -> writerName.getTotalCuriousCount() <= 0);
        return writerNameList;
    }

    @Transactional
    public void createWriterNameInMile(final User user, final Moim moim) {
        writerNameRepository.save(
                WriterName.of(moim, randomWriterNameService.generateRandomWriterName(), user)
        );
    }
}
