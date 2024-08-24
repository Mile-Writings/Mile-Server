package com.mile.moim.service.dto.response;

import com.mile.topic.service.dto.response.TopicResponse;
import java.util.List;

public record TopicListResponse(List<TopicResponse> topicList) {
    public static TopicListResponse of(final List<TopicResponse> categoryList) {
        return new TopicListResponse(categoryList);
    }
}

