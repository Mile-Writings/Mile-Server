package com.mile.topic.serivce.dto;

import com.mile.topic.domain.Topic;

public record ContentResponse(
        Long topicId,
        String topicName
) {
    public static ContentResponse of(
            final Topic topic
    ) {
        return new ContentResponse(topic.getId(), topic.getContent());
    }
}
