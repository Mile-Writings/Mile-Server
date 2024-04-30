package com.mile.topic.service.dto;

import com.mile.topic.domain.Topic;

public record ContentResponse(
        String topicId,
        String topicName
) {
    public static ContentResponse of(
            final Topic topic
    ) {
        return new ContentResponse(topic.getIdUrl(), topic.getContent());
    }
}
