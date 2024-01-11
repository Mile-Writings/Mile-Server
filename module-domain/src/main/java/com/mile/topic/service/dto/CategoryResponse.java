package com.mile.topic.service.dto;

import com.mile.topic.domain.Topic;

public record CategoryResponse(Long categoryId, String categoryName) {
    public static CategoryResponse of(Topic topic) {
        return new CategoryResponse(topic.getId(), topic.getKeyword());
    }
}
