package com.mile.recommend.service.dto;

public record RecommendResponse(
        String content
) {
    public static RecommendResponse of(final String content) {
        return new RecommendResponse(content);
    }
}
