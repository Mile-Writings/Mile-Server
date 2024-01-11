package com.mile.moim.serivce.dto;

import com.mile.topic.serivce.dto.ContentResponse;

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
