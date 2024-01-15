package com.mile.topic.service.dto;

import com.mile.topic.domain.Topic;

public record TopicResponse(String topicId, String topicName) {
    public static TopicResponse of(final Topic topic) {
        return new TopicResponse(topic.getIdUrl(), topic.getKeyword());
    }
}
