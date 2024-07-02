package com.mile.writername.service;

import com.mile.comment.service.CommentRetriever;
import com.mile.comment.service.CommentRemover;
import com.mile.curious.service.CuriousRemover;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.exception.model.ForbiddenException;
import com.mile.moim.domain.Moim;
import com.mile.moim.service.MoimRetriever;
import com.mile.moim.service.dto.MoimWriterNameListGetResponse;
import com.mile.moim.service.dto.WriterMemberJoinRequest;
import com.mile.post.service.PostDeleteService;
import com.mile.post.service.PostGetService;
import com.mile.user.domain.User;
import com.mile.user.service.UserRetriever;
import com.mile.writername.domain.WriterName;
import com.mile.writername.service.dto.WriterNameDescriptionResponse;
import com.mile.writername.service.dto.WriterNameDescriptionUpdateRequest;
import com.mile.writername.service.dto.WriterNameInfoResponse;
import com.mile.writername.service.dto.WriterNameShortResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class WriterNameService {
    private final MoimRetriever moimRetriever;
    private final UserRetriever userRetriever;
    private final CommentRemover commentRemover;
    private final PostDeleteService postDeleteService;
    private final CuriousRemover curiousRemover;
    private final PostGetService postGetService;
    private final CommentRetriever commentGetService;

    private final WriterNameRemover writerNameRemover;
    private final WriterNameRetriever writerNameRetriever;
    private final WriterNameCreator writerNameCreator;

    private static final int WRITERNAME_PER_PAGE_SIZE = 5;
    private static final int WRITERNAME_MAX_SIZE = 5;
    private static final int MIN_TOTAL_CURIOUS_COUNT = 0;

    public WriterNameDescriptionResponse findWriterNameDescription(
            final Long userId,
            final Long writerNameId
    ) {
        WriterName writerName = writerNameRetriever.findById(writerNameId);
        checkWriterNameUserId(userId, writerName);
        return WriterNameDescriptionResponse.of(writerName);
    }

    public void deleteWriterNameById(
            final Long writerNameId,
            final Long userId
    ) {
        WriterName writerName = writerNameRetriever.findById(writerNameId);
        moimRetriever.authenticateOwnerOfMoim(writerName.getMoim(), userRetriever.findById(userId));

        postDeleteService.deleteAllPostByWriterNameId(writerNameId);
        commentRemover.deleteAllCommentByWriterNameId(writerNameId);
        curiousRemover.deleteAllByWriterNameId(writerName);

        writerNameRemover.deleteWriterName(writerName);
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
        return WriterNameShortResponse.of(writerNameRetriever.findByMoimAndUser(moimId, userId));
    }

    @Transactional
    public void updateWriterNameDescription(
            final Long userId,
            final Long writerNameId,
            final WriterNameDescriptionUpdateRequest request
    ) {
        WriterName writerName = writerNameRetriever.findById(writerNameId);
        checkWriterNameUserId(userId, writerName);
        writerName.updateInformation(request);
    }

    private void checkWriterNameOverFive(
            final User user
    ) {
        if (writerNameRetriever.countWriterNameByWriter(user) >= WRITERNAME_MAX_SIZE) {
            throw new BadRequestException(ErrorMessage.EXCEED_MOIM_MAX_SIZE);
        }
    }



    @Transactional
    public Long createWriterName(final User user, final Moim moim, final WriterMemberJoinRequest joinRequest) {
        checkWriterNameOverFive(user);
        WriterName writerName = writerNameCreator.createWriterName(user, moim, joinRequest);
        return writerName.getId();
    }


    public MoimWriterNameListGetResponse getWriterNameInfoList(
            final Moim moim,
            final int page
    ) {
        final Long moimId = moim.getId();
        PageRequest pageRequest = PageRequest.of(page - 1, WRITERNAME_PER_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "id"));
        Page<WriterName> writerNamePage = writerNameRetriever.findWriterNameByMoimIdOrderByLatest(moimId, pageRequest);
        List<WriterNameInfoResponse> infoResponses = writerNamePage.getContent()
                .stream()
                .map(writerName -> WriterNameInfoResponse.of(writerName.getId(), writerName.getName(),
                        postGetService.findPostCountByWriterNameId(writerName.getId()),
                        commentGetService.findCommentCountByWriterNameId(writerName.getId()),
                        writerName.equals(moim.getOwner())))
                .collect(Collectors.toList());

        return MoimWriterNameListGetResponse.of(
                writerNamePage.getTotalPages(),
                writerNameRetriever.findNumbersOfWritersByMoimId(moimId),
                infoResponses
        );
    }

}
