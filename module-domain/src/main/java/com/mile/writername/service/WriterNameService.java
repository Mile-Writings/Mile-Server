package com.mile.writername.service;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.domain.Moim;
import com.mile.moim.service.dto.MoimWriterNameListGetResponse;
import com.mile.moim.service.dto.WriterMemberJoinRequest;
import com.mile.post.domain.Post;
import com.mile.user.domain.User;
import com.mile.writername.domain.WriterName;
import com.mile.writername.repository.WriterNameRepository;
import com.mile.writername.service.dto.WriterNameInfoResponse;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class WriterNameService {
    private final WriterNameRepository writerNameRepository;
    private static final int MIN_TOTAL_CURIOUS_COUNT = 0;
    private static final int WRITERNAME_PER_PAGE_SIZE = 5;

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

    public boolean existWriterNamesByMoimAndName(
            final Moim moim,
            final String name
    ) {
        return writerNameRepository.existsWriterNameByMoimAndName(moim, name);
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
    public Long createWriterName(final User user, final Moim moim, final WriterMemberJoinRequest joinRequest) {
        WriterName writerName = WriterName.of(moim, joinRequest, user);
        writerNameRepository.saveAndFlush(writerName);
        return writerName.getId();
    }

    private List<WriterName> findAllByMoimId(
            final Long moimId
    ) {
        return writerNameRepository.findByMoimId(moimId);
    }

    public MoimWriterNameListGetResponse getWriterNameInfoList(
            final Long moimId,
            final int page
    ) {
        PageRequest pageRequest = PageRequest.of(page-1, WRITERNAME_PER_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "id"));
        Page<WriterName> writerNamePage = writerNameRepository.findByMoimIdOrderByIdDesc(moimId, pageRequest);
        List<WriterNameInfoResponse> infoResponses = writerNamePage.getContent()
                .stream()
                .map(writerName -> WriterNameInfoResponse.of(writerName.getId(), writerName.getName(), writerName.getInformation()))
                .collect(Collectors.toList());

        return MoimWriterNameListGetResponse.of(
                writerNamePage.getTotalPages(),
                findNumbersOfWritersByMoimId(moimId),
                infoResponses
        );
    }
}
