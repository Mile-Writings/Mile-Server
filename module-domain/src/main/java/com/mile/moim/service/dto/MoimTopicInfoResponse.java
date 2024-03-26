package com.mile.moim.service.dto;

import com.mile.topic.domain.Topic;
import com.mile.utils.DateUtil;

public record MoimTopicInfoResponse(
    String topicId,
    String topicName,
    String topicTag,
    String topicDescription,
    String createdAt
) {
    public static MoimTopicInfoResponse of(
            final Topic topic
    ) {
        return new MoimTopicInfoResponse(
                topic.getIdUrl(),
                topic.getContent(),
                topic.getKeyword(),
                topic.getDescription(),
                DateUtil.getStringDateOfLocalDate(topic.getCreatedAt())
        );
    }
}
