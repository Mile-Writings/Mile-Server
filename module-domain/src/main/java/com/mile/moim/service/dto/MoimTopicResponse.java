package com.mile.moim.service.dto;

public record MoimTopicResponse(
        String content
) {

    public static MoimTopicResponse of(
            final String content
    ) {
        return new MoimTopicResponse(content);
    }
}
