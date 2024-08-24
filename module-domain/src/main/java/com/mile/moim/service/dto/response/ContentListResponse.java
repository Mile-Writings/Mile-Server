package com.mile.moim.service.dto.response;

import com.mile.topic.service.dto.response.ContentResponse;

import java.util.List;

public record ContentListResponse(
        List<ContentResponse> topics
) {
    public static ContentListResponse of(
            final List<ContentResponse> topics
    ) {
        return new ContentListResponse(topics);
    }
}
