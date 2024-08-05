package com.mile.topic.service.dto;

import com.mile.topic.domain.Topic;

public record TopicResponse(Long id, String topicName) {
    public static TopicResponse of(final Topic topic) {
        return new TopicResponse(topic.getId(), topic.getKeyword());
    }
}
