package com.mile.moim.serivce;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.ForbiddenException;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.domain.Moim;
import com.mile.moim.repository.MoimRepository;
import com.mile.moim.serivce.dto.ContentListResponse;
import com.mile.moim.serivce.dto.MoimAuthenticateResponse;
import com.mile.moim.serivce.dto.MoimTopicResponse;
import com.mile.moim.serivce.dto.MoimInfoResponse;
import com.mile.topic.serivce.TopicService;
import com.mile.utils.DateUtil;
import com.mile.writerName.domain.WriterName;
import com.mile.writerName.serivce.WriterNameService;
import com.mile.writerName.serivce.dto.PopularWriterListResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoimService {

    private final WriterNameService writerNameService;
    private final TopicService topicService;
    private final MoimRepository moimRepository;

    private static final int NUMBER_OF_MOST_CURIOUS_WRITERS = 2;

    public ContentListResponse getContentsFromMoim(
            final Long moimId,
            final Long userId
    ) {
        authenticateUserOfMoim(moimId, userId);
        return ContentListResponse.of(topicService.getContentsFromMoim(moimId));
    }

    public void authenticateUserOfMoim(
            final Long moimId,
            final Long userId
    ) {
        if (!writerNameService.isUserInMoim(moimId, userId)) {
            throw new ForbiddenException(ErrorMessage.USER_AUTHENTICATE_ERROR);
        }
    }

    public MoimAuthenticateResponse getAuthenticateUserOfMoim(
            final Long moimId,
            final Long userId
    ) {
        return MoimAuthenticateResponse.of(writerNameService.isUserInMoim(moimId, userId));
    }
    private Moim findById(
            final Long moimId
    ) {
        return moimRepository.findById(moimId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MOIM_NOT_FOUND)
        );
    }

    public PopularWriterListResponse getMostCuriousWriters(
            final Long moimId
    ) {
        List<WriterName> writers = writerNameService.findTop2ByCuriousCount(moimId);
        checkSizeOfWriters(writers);
        return PopularWriterListResponse.of(writers);
    }

    public void checkSizeOfWriters(
            List<WriterName> writersOfMoim
    ) {
        if (writersOfMoim.size() < NUMBER_OF_MOST_CURIOUS_WRITERS) {
            throw new NotFoundException(ErrorMessage.WRITERS_NOT_FOUND);
        }
    }

    public MoimTopicResponse getTopicFromMoim(
            final Long moimId
    ) {
        return MoimTopicResponse.of(topicService.findLatestTopicByMoim(findById(moimId)));
    }

    public MoimInfoResponse getMoimInfo(
            final Long moimId
    ) {
        Moim moim = findById(moimId);
        return MoimInfoResponse.of(
                moim.getImageUrl(),
                moim.getName(),
                writerNameService.getOwnerNameOfMoimId(moimId),
                moim.getInformation(),
                writerNameService.findNumbersOfWritersByMoimId(moimId),
                DateUtil.getStringDateOfLocalDate(moim.getCreatedAt())
        );
    }
}
