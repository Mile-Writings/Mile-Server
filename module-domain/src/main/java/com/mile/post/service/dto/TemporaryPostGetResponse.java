package com.mile.post.service.dto;

import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import com.mile.utils.DateUtil;


public record TemporaryPostGetResponse(
        TemporaryPostTopicGetResponse topic,
        String title,
        String content,
        String imageUrl,
        boolean anonymous
) {
    public static TemporaryPostGetResponse of(Post post, Moim moim) {

        return new TemporaryPostGetResponse(
                TemporaryPostTopicGetResponse.of(post.getTopic()),
                post.getTitle(),
                post.getContent(),
                post.getImageUrl(),
                post.isAnonymous()
        );
    }
}