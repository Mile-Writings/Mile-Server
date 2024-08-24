package com.mile.post.service.dto.response;

import com.mile.post.domain.Post;
import com.mile.topic.service.dto.response.ContentWithIsSelectedResponse;
import java.util.List;


public record TemporaryPostGetResponse(
        List<ContentWithIsSelectedResponse> topicList,
        String title,
        String content,
        String imageUrl,
        boolean anonymous
) {
    public static TemporaryPostGetResponse of(Post post, List<ContentWithIsSelectedResponse> contentResponse) {
        return new TemporaryPostGetResponse(
                contentResponse,
                post.getTitle(),
                post.getContent(),
                post.getImageUrl(),
                post.isAnonymous()
        );
    }


}