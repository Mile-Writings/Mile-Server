package com.mile.writername.service;

import com.mile.comment.service.CommentGetService;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.exception.model.ConflictException;
import com.mile.exception.model.ForbiddenException;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.domain.Moim;
import com.mile.moim.service.dto.MoimWriterNameListGetResponse;
import com.mile.moim.service.dto.WriterMemberJoinRequest;
import com.mile.post.domain.Post;
import com.mile.post.service.PostGetService;
import com.mile.user.domain.User;
import com.mile.writername.domain.WriterName;
import com.mile.writername.repository.WriterNameRepository;
import com.mile.writername.service.dto.WriterNameDescriptionResponse;
import com.mile.writername.service.dto.WriterNameDescriptionUpdateRequest;
import com.mile.writername.service.dto.WriterNameInfoResponse;
import com.mile.writername.service.dto.WriterNameShortResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class WriterNameService {
    private final WriterNameRepository writerNameRepository;
    private final PostGetService postGetService;
    private final CommentGetService commentGetService;
    private static final int MIN_TOTAL_CURIOUS_COUNT = 0;
    private static final int WRITERNAME_PER_PAGE_SIZE = 5;
    private static final int WRITERNAME_MAX_SIZE = 5;

    public WriterName findById(
            final Long writerNameId
    ) {
        return writerNameRepository.findById(writerNameId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.WRITER_NOT_FOUND)
                );
    }

    public boolean isUserInMoim(
            final Long moimId,
            final Long writerId
    ) {
        return writerNameRepository.existsWriterNameByMoimIdAndWriterId(moimId, writerId);
    }

    public WriterNameDescriptionResponse findWriterNameDescription(
            final Long userId,
            final Long writerNameId
    ) {
        WriterName writerName = findById(writerNameId);
        checkWriterNameUserId(userId, writerName);
        return WriterNameDescriptionResponse.of(writerName);
    }

    private void checkWriterNameUserId(final Long userId, final WriterName writerName) {
        if (!writerName.getWriter().getId().equals(userId)) {
            throw new ForbiddenException(ErrorMessage.WRITER_NAME_INFO_FORBIDDEN);
        }
    }

    public WriterNameShortResponse findWriterNameInfo(
            final Long moimId,
            final Long userId
    ) {
        return WriterNameShortResponse.of(findByMoimAndUser(moimId, userId));
    }

    @Transactional
    public void updateWriterNameDescription(
            final Long userId,
            final Long writerNameId,
            final WriterNameDescriptionUpdateRequest request
    ) {
        WriterName writerName = findById(writerNameId);
        checkWriterNameUserId(userId, writerName);
        writerName.updateInformation(request);
    }

    private void checkWriterNameOverFive(
            final User user
    ) {
        if (writerNameRepository.countAllByWriter(user) >= WRITERNAME_MAX_SIZE) {
            throw new BadRequestException(ErrorMessage.EXCEED_MOIM_MAX_SIZE);
        }
    }

    public void deleteWriterNameByUserId(
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

    public Optional<WriterName> findMemberByMoimIdAndWriterId(
            final Long moimId,
            final Long writerId
    ) {
        return writerNameRepository.findByMoimIdAndWriterId(moimId, writerId);
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
        return writerNameRepository.existsWriterNameByMoimAndNormalizedName(moim, name);
    }

    public WriterName findByWriterId(
            final Long writerId
    ) {
        return writerNameRepository.findByWriterId(writerId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.WRITER_NOT_FOUND)
                );
    }

    public void decreaseTotalCuriousCountByWriterName(
            final WriterName writerName
    ) {
        writerName.decreaseTotalCuriousCount();
    }

    public void increaseTotalCuriousCountByWriterName(
            final WriterName writerName
    ) {
        writerName.increaseTotalCuriousCount();
    }


    public WriterName findWriterNameByMoimIdAndUserId(
            final Long moimId,
            final Long userId
    ) {
        return getById(getWriterNameIdByMoimIdAndUserId(moimId, userId));
    }

    public List<WriterName> findTop2ByCuriousCount(final Long moimid) {
        return writerNameRepository.findTop2ByMoimIdAndTotalCuriousCountGreaterThanOrderByTotalCuriousCountDesc(moimid, MIN_TOTAL_CURIOUS_COUNT);
    }

    @Transactional
    public Long createWriterName(final User user, final Moim moim, final WriterMemberJoinRequest joinRequest) {
        checkWriterNameOverFive(user);
        WriterName writerName;
        try {
            writerName = writerNameRepository.save(WriterName.of(moim, joinRequest, user));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(ErrorMessage.WRITER_NAME_ALREADY_EXIST);
        }
        return writerName.getId();
    }

    public WriterName getById(final Long writerNameId) {
        return writerNameRepository.findById(writerNameId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.WRITER_NOT_FOUND)
                );
    }


    public MoimWriterNameListGetResponse getWriterNameInfoList(
            final Moim moim,
            final int page
    ) {
        final Long moimId = moim.getId();
        PageRequest pageRequest = PageRequest.of(page - 1, WRITERNAME_PER_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "id"));
        Page<WriterName> writerNamePage = writerNameRepository.findByMoimIdOrderByIdDesc(moimId, pageRequest);
        List<WriterNameInfoResponse> infoResponses = writerNamePage.getContent()
                .stream()
                .map(writerName -> WriterNameInfoResponse.of(writerName.getId(), writerName.getName(),
                        postGetService.findPostCountByWriterNameId(writerName.getId()),
                        commentGetService.findCommentCountByWriterNameId(writerName.getId()),
                        writerName.equals(moim.getOwner())))
                .collect(Collectors.toList());

        return MoimWriterNameListGetResponse.of(
                writerNamePage.getTotalPages(),
                findNumbersOfWritersByMoimId(moimId),
                infoResponses
        );
    }

    public List<Moim> getMoimListOfUser(
            final Long userId
    ) {
        return writerNameRepository.findAllByWriterId(userId)
                .stream()
                .map(writerName -> writerName.getMoim())
                .collect(Collectors.toList());
    }
}
