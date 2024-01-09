package com.mile.post.service.dto;

public record PostPutRequest(
        Long topicId,
        String title,
        String content,
        String imageUrl,
        boolean anonymous
) {
}
