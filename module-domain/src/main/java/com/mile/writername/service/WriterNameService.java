package com.mile.writername.service;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import com.mile.user.domain.User;
import com.mile.writername.domain.WriterName;
import com.mile.writername.repository.WriterNameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class WriterNameService {
    private final WriterNameRepository writerNameRepository;
    private final RandomWriterNameService randomWriterNameService;
    private static final int MIN_TOTAL_CURIOUS_COUNT = 0;

    public boolean isUserInMoim(
            final Long moimId,
            final Long writerId
    ) {
        return writerNameRepository.existsWriterNameByMoimIdAndWriterId(moimId, writerId);
    }

    public void deleteWriterName(
            final Long userId
    ) {
        writerNameRepository.delete(findByWriterId(userId));
    }
    public Long getWriterNameIdByMoimIdAndUserId(
            final Long moimId,
            final Long userId
    ) {
        return findByMoimAndUser(moimId, userId).getId();
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

    public WriterName getWriterNameByPostAndUserId(
            final Post post,
            final Long userId
    ) {
        return writerNameRepository.findByMoimIdAndWriterId(post.getTopic().getMoim().getId(), userId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.WRITER_NOT_FOUND)
                );
    }
    public int findNumbersOfWritersByMoimId(
            final Long moimId
    ) {
        return writerNameRepository.findByMoimId(moimId).size();
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
        return writerNameRepository.findTop2ByMoimIdAndTotalCuriousCountGreaterThanOrderByTotalCuriousCountDesc(moimid, MIN_TOTAL_CURIOUS_COUNT);
    }

    @Transactional
    public void createWriterNameInMile(final User user, final Moim moim) {
        writerNameRepository.save(
                WriterName.of(moim, randomWriterNameService.generateRandomWriterName(), user)
        );
    }
}
