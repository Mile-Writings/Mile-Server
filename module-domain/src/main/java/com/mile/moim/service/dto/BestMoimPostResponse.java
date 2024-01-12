package com.mile.moim.service.dto;

import com.mile.post.domain.Post;

public record BestMoimPostResponse(String topicName, String imageUrl, String postTitle, String postContent) {
    public static BestMoimPostResponse of(Post post) {
        return new BestMoimPostResponse(
                post.getTopic().getContent(),
                post.getImageUrl(),
                post.getTitle(),
                post.getContent()
        );
    }
}
