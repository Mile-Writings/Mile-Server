package com.mile.moim.serivce.dto;

import com.mile.post.domain.Post;

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
