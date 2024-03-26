package com.mile.moim.service.dto;

import java.util.List;

public record MoimTopicInfoListResponse(
        Long topicCount,
        List<MoimTopicInfoResponse> topics
) {
    public static MoimTopicInfoListResponse of (
            final Long topicCount,
            final List<MoimTopicInfoResponse> moimTopicInfoResponses
    ) {
        return new MoimTopicInfoListResponse(
                topicCount,
                moimTopicInfoResponses
        );
    }
}
