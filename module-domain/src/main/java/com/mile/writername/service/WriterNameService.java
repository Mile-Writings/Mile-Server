package com.mile.writername.service;

import com.mile.comment.service.CommentRetriever;
import com.mile.commentreply.service.CommentReplyRetriever;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.exception.model.ConflictException;
import com.mile.exception.model.ForbiddenException;
import com.mile.moim.domain.Moim;
import com.mile.moim.service.MoimRemover;
import com.mile.moim.service.MoimRetriever;
import com.mile.moim.service.dto.response.MoimWriterNameListGetResponse;
import com.mile.moim.service.dto.request.WriterMemberJoinRequest;
import com.mile.post.service.PostRetriever;
import com.mile.user.domain.User;
import com.mile.user.service.UserRetriever;
import com.mile.writername.domain.WriterName;
import com.mile.writername.service.dto.response.WriterNameDescriptionResponse;
import com.mile.writername.service.dto.request.WriterNameDescriptionUpdateRequest;
import com.mile.writername.service.dto.response.WriterNameInfoResponse;
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
    private final MoimRetriever moimRetriever;
    private final UserRetriever userRetriever;
    private final PostRetriever postRetriever;
    private final CommentRetriever commentRetriever;
    private final WriterNameRemover writerNameRemover;
    private final WriterNameRetriever writerNameRetriever;
    private final WriterNameCreator writerNameCreator;
    private final MoimRemover moimRemover;

    private static final int WRITERNAME_PER_PAGE_SIZE = 5;
    private static final int WRITERNAME_MAX_SIZE = 10;
    private final CommentReplyRetriever commentReplyRetriever;

    public WriterNameDescriptionResponse findWriterNameDescription(
            final Long userId,
            final Long writerNameId
    ) {
        WriterName writerName = writerNameRetriever.findById(writerNameId);
        checkWriterNameUserId(userId, writerName);
        return WriterNameDescriptionResponse.of(writerName);
    }
    @Transactional
    public void deleteWriterNameByUser(final User user) {
        writerNameRetriever.findByWriter(user).forEach(
                writerName -> {
                    writerNameRemover.deleteRelatedData(writerName);
                    moimRemover.deleteMoimByOwner(writerName);
                }
        );
    }


    public void deleteWriterNameById(
            final Long writerNameId,
            final Long userId
    ) {
        WriterName writerName = writerNameRetriever.findById(writerNameId);
        moimRetriever.authenticateOwnerOfMoim(writerName.getMoim(), userRetriever.findById(userId));
        writerNameRemover.deleteWriterName(writerName);
    }


    private void checkWriterNameUserId(final Long userId, final WriterName writerName) {
        if (!writerName.getWriter().getId().equals(userId)) {
            throw new ForbiddenException(ErrorMessage.WRITER_NAME_INFO_FORBIDDEN);
        }
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
        validateWriterNameUniqueInMoim(moim, joinRequest.writerName());
        WriterName writerName = writerNameCreator.createWriterName(user, moim, joinRequest);
        return writerName.getId();
    }

    private void validateWriterNameUniqueInMoim(Moim moim, String writerName) {
        String normalizedWriterName = writerName.replaceAll("\\s+", "").toLowerCase();
        if (writerNameRetriever.existWriterNamesByMoimAndName(moim, normalizedWriterName)) {
            throw new ConflictException(ErrorMessage.WRITER_NAME_OF_MOIM_ALREADY_EXISTS_EXCEPTION);
        }
    }

    public MoimWriterNameListGetResponse getWriterNameInfoList(
            final Moim moim,
            final int page
    ) {
        final Long moimId = moim.getId();
        PageRequest pageRequest = PageRequest.of(page - 1, WRITERNAME_PER_PAGE_SIZE, Sort.by(Sort.Direction.ASC, "id"));
        Page<WriterName> writerNamePage = writerNameRetriever.findWriterNameByMoimIdOrderByOwnerFirstAndIdAsc(moimId, moim.getOwner(), pageRequest);
        List<WriterNameInfoResponse> infoResponses = writerNamePage.getContent()
                .stream()
                .map(writerName -> WriterNameInfoResponse.of(writerName.getId(), writerName.getName(),
                        postRetriever.findPostCountByWriterNameId(writerName.getId()),
                        commentRetriever.findCommentCountByWriterName(writerName) + commentReplyRetriever.countByWriterName(writerName),
                        writerName.equals(moim.getOwner())))
                .toList();

        return MoimWriterNameListGetResponse.of(
                writerNamePage.getTotalPages(),
                writerNameRetriever.findNumbersOfWritersByMoimId(moimId),
                infoResponses
        );
    }

}
