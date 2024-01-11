package com.mile.topic.serivce.dto;

import com.mile.post.service.dto.PostListResponse;

import java.util.List;

public record PostListInTopicResponse(
        TopicOfMoimResponse topicInfo,
        List<PostListResponse> postList
) {
    public static PostListInTopicResponse of(
            final TopicOfMoimResponse topicInfo,
            final List<PostListResponse> postList
    ) {
        return new PostListInTopicResponse(topicInfo, postList);
    }
}
