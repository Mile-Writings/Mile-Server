package com.mile.writername.service;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.ForbiddenException;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.domain.Moim;
import com.mile.user.domain.User;
import com.mile.writername.domain.WriterName;
import com.mile.writername.repository.WriterNameRepository;
import com.mile.writername.service.dto.response.WriterNameShortResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WriterNameRetriever {

    private final WriterNameRepository writerNameRepository;
    private static final int MIN_TOTAL_CURIOUS_COUNT = 0;

    public boolean existWriterNamesByMoimAndName(
            final Moim moim,
            final String name
    ) {
        return writerNameRepository.existsWriterNameByMoimAndNormalizedName(moim, name);
    }

    public List<WriterName> findByWriter(final User user) {
        return writerNameRepository.findByWriter(user);
    }

    public WriterName findById(final Long id) {
        return writerNameRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ErrorMessage.WRITER_NOT_FOUND)
        );
    }

    public WriterNameShortResponse findWriterNameInfo(
            final Long moimId,
            final Long userId
    ) {
        return WriterNameShortResponse.of(findByMoimAndUserWithNotExceptionCase(moimId, userId));
    }

    public boolean isUserInMoim(
            final Long moimId,
            final Long writerId
    ) {
        return writerNameRepository.existsWriterNameByMoimIdAndWriterId(moimId, writerId);
    }

    public int countWriterNameByWriter(final User user) {
        return writerNameRepository.countAllByWriter(user);
    }

    public WriterName findByMoimAndUser(
            final Long moimId,
            final Long writerId
    ) {
        return writerNameRepository.findByMoimIdAndWriterId(moimId, writerId)
                .orElseThrow(
                        () -> new ForbiddenException(ErrorMessage.USER_MOIM_AUTHENTICATE_ERROR)
                );
    }

    public WriterName findByMoimAndUserWithNotExceptionCase(
            final Long moimId,
            final Long writerId
    ) {
        return writerNameRepository.findByMoimIdAndWriterId(moimId, writerId)
                .orElseThrow( () -> new ForbiddenException(ErrorMessage.WRITER_NAME_NON_AUTHENTICATE)
                );
    }

    public Long getWriterNameIdByMoimIdAndUserId(
            final Long moimId,
            final Long userId
    ) {
        return findByMoimAndUser(moimId, userId).getId();
    }

    public List<Moim> getMoimListOfUser(
            final Long userId
    ) {
        return writerNameRepository.findAllByWriterId(userId)
                .stream()
                .map(WriterName::getMoim)
                .collect(Collectors.toList());
    }

    public Optional<WriterName> findMemberByMoimIdAndWriterId(
            final Long moimId,
            final Long writerId
    ) {
        return writerNameRepository.findByMoimIdAndWriterId(moimId, writerId);
    }

    public WriterName findWriterNameByMoimIdAndUserId(
            final Long moimId,
            final Long userId
    ) {
        return findById(getWriterNameIdByMoimIdAndUserId(moimId, userId));
    }

    public int findNumbersOfWritersByMoimId(
            final Long moimId
    ) {
        return writerNameRepository.findByMoimId(moimId).size();
    }

    public int findNumbersOfWritersByMoim(
            final Moim moim
    ) {
        return writerNameRepository.countByMoim(moim);
    }

    public List<WriterName> findTop2ByCuriousCount(final Moim moim) {
        return writerNameRepository.findTop2ByMoimAndTotalCuriousCountGreaterThanOrderByTotalCuriousCountDesc(moim, MIN_TOTAL_CURIOUS_COUNT);
    }


    public Page<WriterName> findWriterNameByMoimIdOrderByOwnerFirstAndIdAsc(final Long moimId, final WriterName owner, final PageRequest pageRequest) {
        return writerNameRepository.findByMoimIdOrderByOwnerFirstAndIdAsc(moimId, owner, pageRequest);
    }

    public List<WriterName> findCuriousWriterNameNotIn(final Moim moim, final List<WriterName> writerNames) {
        return writerNameRepository.findCuriousWriterNameNotIn(moim, writerNames, 2 - writerNames.size());
    }
}
