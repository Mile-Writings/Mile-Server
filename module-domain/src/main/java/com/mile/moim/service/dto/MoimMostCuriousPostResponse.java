package com.mile.moim.service.dto;

public record MoimMostCuriousPostResponse(
        String imageUrl,
        String topic,
        String title,
        String content
) {
    public static MoimMostCuriousPostResponse of(
            String imageUrl,
            String topic,
            String title,
            String content
    ) {
        return new MoimMostCuriousPostResponse(imageUrl, topic, title, content);
    }
}
