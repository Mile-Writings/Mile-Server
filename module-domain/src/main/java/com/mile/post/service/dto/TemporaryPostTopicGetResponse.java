package com.mile.post.service.dto;

import com.mile.topic.domain.Topic;

public record TemporaryPostTopicGetResponse(String topicId, String topicContent) {
    public static TemporaryPostTopicGetResponse of(Topic topic) {
        return new TemporaryPostTopicGetResponse(topic.getIdUrl(), topic.getContent());
    }
}
