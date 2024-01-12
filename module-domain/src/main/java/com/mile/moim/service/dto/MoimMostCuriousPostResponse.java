package com.mile.moim.service.dto;

public record MoimMostCuriousPostResponse(
        Long postId,
        String imageUrl,
        String topic,
        String title,
        String content
) {
    public static MoimMostCuriousPostResponse of(
            Long postId,
            String imageUrl,
            String topic,
            String title,
            String content
    ) {
        return new MoimMostCuriousPostResponse(postId, imageUrl, topic, title, content);
    }
}
