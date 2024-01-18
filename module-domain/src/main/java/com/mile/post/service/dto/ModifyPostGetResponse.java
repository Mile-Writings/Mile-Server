package com.mile.post.service.dto;

import com.mile.post.domain.Post;
import com.mile.topic.service.dto.ContentWithIsSelectedResponse;
import java.util.List;

public record ModifyPostGetResponse(
        List<ContentWithIsSelectedResponse> topicList,
        String title,
        String content,
        String imageUrl,
        boolean anonymous
) {
    public static ModifyPostGetResponse of(Post post, List<ContentWithIsSelectedResponse> contentResponse) {
        return new ModifyPostGetResponse(
                contentResponse,
                post.getTitle(),
                post.getContent(),
                post.getImageUrl(),
                post.isAnonymous()
        );
    }
}

