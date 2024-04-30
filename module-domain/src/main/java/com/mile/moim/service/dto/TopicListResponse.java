package com.mile.moim.service.dto;

import com.mile.topic.service.dto.TopicResponse;
import java.util.List;

public record TopicListResponse(List<TopicResponse> topicList) {
    public static TopicListResponse of(final List<TopicResponse> categoryList) {
        return new TopicListResponse(categoryList);
    }
}

