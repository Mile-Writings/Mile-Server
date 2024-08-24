package com.mile.topic.service.dto.response;

import com.mile.topic.domain.Topic;

public record TopicOfMoimResponse(
        String topic,
        String topicDescription
) {
    public static TopicOfMoimResponse of(
            Topic topic
    ) {
        return new TopicOfMoimResponse(topic.getContent(), topic.getDescription());
    }
}
