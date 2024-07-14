package com.mile.writername.service;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.ForbiddenException;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import com.mile.user.domain.User;
import com.mile.writername.domain.WriterName;
import com.mile.writername.repository.WriterNameRepository;
import com.mile.writername.service.dto.WriterNameShortResponse;
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

    public WriterName getWriterNameByPostAndUserId(
            final Post post,
            final Long userId
    ) {
        return writerNameRepository.findByMoimIdAndWriterId(post.getTopic().getMoim().getId(), userId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.WRITER_NOT_FOUND)
                );
    }

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
        return WriterNameShortResponse.of(findByMoimAndUser(moimId, userId));
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
                        () -> new ForbiddenException(ErrorMessage.USER_AUTHENTICATE_ERROR)
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
                .map(writerName -> writerName.getMoim())
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

    public List<WriterName> findTop2ByCuriousCount(final Long moimid) {
        return writerNameRepository.findTop2ByMoimIdAndTotalCuriousCountGreaterThanOrderByTotalCuriousCountDesc(moimid, MIN_TOTAL_CURIOUS_COUNT);
    }


    public Page<WriterName> findWriterNameByMoimIdOrderByLatest(final Long moimId, final PageRequest pageRequest) {
        return writerNameRepository.findByMoimIdOrderByIdDesc(moimId, pageRequest);
    }
}
