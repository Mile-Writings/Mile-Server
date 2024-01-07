package com.mile.moim.serivce;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.ForbiddenException;
import com.mile.moim.serivce.dto.ContentListResponse;
import com.mile.topic.serivce.TopicService;
import com.mile.writerName.serivce.WriterNameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoimService {
    private final WriterNameService writerNameService;
    private final TopicService topicService;

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
}
