package com.mile.moim.service.dto;

import java.util.List;

public record MoimTopicInfoListResponse(
        int topicCount,
        List<MoimTopicInfoResponse> topics
) {
    public static MoimTopicInfoListResponse of (
            final List<MoimTopicInfoResponse> moimTopicInfoResponses
    ) {
        return new MoimTopicInfoListResponse(
                moimTopicInfoResponses.size(),
                moimTopicInfoResponses
        );
    }
}
