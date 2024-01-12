package com.mile.post.service.dto;

import com.mile.post.domain.Post;
import com.mile.utils.DateUtil;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

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
    private static final int SUBSTRING_END = 200;

    public static PostListResponse of(final Post post) {
        return new PostListResponse(post.getId(), post.getTitle(), getSubString(post),
                post.getWriterName().getName(),
                DateUtil.getStringWithTimeOfLocalDate(post.getCreatedAt()),
                post.getCuriousCount(),
                post.getImageUrl());
    }

    private static String getSubString(final Post post) {
        String cleanContent = Jsoup.clean(post.getContent(), Whitelist.none());
        if (cleanContent.length() >= SUBSTRING_END) {
            return cleanContent.substring(SUBSTRING_START, SUBSTRING_END);
        } else {
            return cleanContent;
        }
    }
}
