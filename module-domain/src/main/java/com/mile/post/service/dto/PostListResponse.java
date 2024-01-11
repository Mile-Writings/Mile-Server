package com.mile.post.service.dto;

import com.mile.post.domain.Post;
import com.mile.utils.DateUtil;

public record PostListResponse(
        Long postId,
        String postTitle,
        String postContent,
        String writerName,
        String createdAt,
        int curiousCount,
        String imageUrl
) {
    private static final int SUBSTRING_START = 0;
    private static final int SUBSTRING_END_WITH_IMAGE = 104;
    private static final int SUBSTRING_END_WITHOUT_IMAGE = 166;

    public static PostListResponse of(final Post post) {
        return new PostListResponse(post.getId(), post.getTitle(), getSubString(post),
                post.getWriterName().getName(),
                DateUtil.getStringWithTimeOfLocalDate(post.getCreatedAt()),
                post.getCuriousCount(),
                post.getImageUrl());
    }

    private static String getSubString(final Post post) {
        if (post.getImageUrl().isEmpty()) {
            return post.getContent().substring(SUBSTRING_START, SUBSTRING_END_WITHOUT_IMAGE);
        } else {
            return post.getContent().substring(SUBSTRING_START, SUBSTRING_END_WITH_IMAGE);
        }
    }
}
