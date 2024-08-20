package com.mile.topic.service.dto.response;

import com.mile.topic.domain.Topic;
import com.mile.utils.DateUtil;

public record TopicDetailResponse(
        String topicName,
        String topicTag,
        String topicDescription,
        String createdAt
) {

    public static TopicDetailResponse of(final Topic topic){
        return new TopicDetailResponse(topic.getContent(), topic.getKeyword(), topic.getDescription(), DateUtil.getStringDateWithYear(topic.getCreatedAt()));
    }
}
