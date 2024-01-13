package com.mile.topic.service.dto;

import com.mile.topic.domain.Topic;

public record ContentWithIsSelectedResponse(
        Long topicid,
        String topicName,
        boolean isSelected
) {
    public static ContentWithIsSelectedResponse of(
            final Topic topic,
            final boolean isSelected
    ) {
        return new ContentWithIsSelectedResponse(topic.getId(), topic.getContent(), isSelected);
    }
}

