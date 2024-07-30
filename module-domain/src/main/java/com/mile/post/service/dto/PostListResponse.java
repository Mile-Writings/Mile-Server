package com.mile.post.service.dto;

import com.mile.post.domain.Post;
import com.mile.utils.DateUtil;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public record PostListResponse(
        String postId,
        String postTitle,
        String postContent,
        String writerName,
        String createdAt,
        int curiousCount,
        int hitsCount,
        int commentCount,
        String imageUrl,
        Boolean isImageContained
) {
    private static final int SUBSTRING_START = 0;
    private static final int SUBSTRING_END = 400;

    public static PostListResponse of(final Post post, final int commentCount) {
        return new PostListResponse(post.getIdUrl(), post.getTitle(), getSubString(post),
                post.getWriterName().getName(),
                DateUtil.getStringWithTimeOfLocalDate(post.getCreatedAt()),
                post.getCuriousCount(),
                post.getHitsCount(),
                commentCount,
                post.getImageUrl(),
                post.isContainPhoto());
    }

    private static String getSubString(final Post post) {
        String cleanContent = Jsoup.clean(post.getContent(), Safelist.none());
        if (cleanContent.length() >= SUBSTRING_END) {
            return cleanContent.substring(SUBSTRING_START, SUBSTRING_END);
        } else {
            return cleanContent;
        }
    }
}
