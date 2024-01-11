package com.mile.moim.dto;

import com.mile.topic.service.dto.ContentResponse;

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
