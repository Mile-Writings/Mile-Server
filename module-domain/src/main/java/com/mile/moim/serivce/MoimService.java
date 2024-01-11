package com.mile.moim.serivce;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.ForbiddenException;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.domain.Moim;
import com.mile.moim.repository.MoimRepository;
import com.mile.moim.serivce.dto.CategoryListResponse;
import com.mile.moim.serivce.dto.ContentListResponse;
import com.mile.moim.serivce.dto.MoimAuthenticateResponse;
import com.mile.moim.serivce.dto.MoimInfoResponse;
import com.mile.topic.serivce.TopicService;
import com.mile.topic.serivce.dto.CategoryResponse;
import com.mile.utils.DateUtil;
import com.mile.writerName.serivce.WriterNameService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoimService {
    private final WriterNameService writerNameService;
    private final TopicService topicService;
    private final MoimRepository moimRepository;

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

    private Moim findById(
            final Long moimId
    ) {
        return moimRepository.findById(moimId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MOIM_NOT_FOUND)
        );
    }

    public CategoryListResponse getCategoryList(
            final Long moimId
    ) {
        return CategoryListResponse.of(topicService.getKeywordsFromMoim(moimId));
    }
}
