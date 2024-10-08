package com.mile.topic.service.dto.response;

import com.mile.topic.domain.Topic;

public record ContentWithIsSelectedResponse(
        String topicId,
        String topicName,
        boolean isSelected
) {
    public static ContentWithIsSelectedResponse of(
            final Topic topic,
            final boolean isSelected
    ) {
        return new ContentWithIsSelectedResponse(topic.getIdUrl(), topic.getContent(), isSelected);
    }
}

