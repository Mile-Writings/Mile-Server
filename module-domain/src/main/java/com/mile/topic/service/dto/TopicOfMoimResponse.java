package com.mile.topic.serivce.dto;

import com.mile.topic.domain.Topic;

public record TopicOfMoimResponse(
        String topic,
        String topicDecription
) {
    public static TopicOfMoimResponse of(
            Topic topic
    ) {
        return new TopicOfMoimResponse(topic.getContent(), topic.getDescription());
    }
}
