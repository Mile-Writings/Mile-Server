package com.mile.moim.service.dto.response;

import java.util.List;

public record MoimTopicInfoListResponse(
        int pageNumber,
        Long topicCount,
        List<MoimTopicInfoResponse> topics
) {
    public static MoimTopicInfoListResponse of (
            final int pageNumber,
            final Long topicCount,
            final List<MoimTopicInfoResponse> moimTopicInfoResponses
    ) {
        return new MoimTopicInfoListResponse(
                pageNumber,
                topicCount,
                moimTopicInfoResponses
        );
    }
}
