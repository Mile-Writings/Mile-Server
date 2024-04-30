package com.mile.moim.service.dto;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public record MoimMostCuriousPostResponse(
        String postId,
        String imageUrl,
        String topic,
        String title,
        String content,
        boolean isContainPhoto
) {
    private static final int SUBSTRING_START = 0;
    private static final int SUBSTRING_END = 400;

    public static MoimMostCuriousPostResponse of(
            String postId,
            String imageUrl,
            String topic,
            String title,
            String content,
            boolean isContainPhoto
    ) {
        return new MoimMostCuriousPostResponse(postId, imageUrl, topic, title, getSubStringOfCleanContent(content), isContainPhoto);
    }

    private static String getSubStringOfCleanContent(
            String content
    ) {
        String cleanContent = Jsoup.clean(content, Whitelist.none());
        if (cleanContent.length() >= SUBSTRING_END) {
            return cleanContent.substring(SUBSTRING_START, SUBSTRING_END);
        } else {
            return cleanContent;
        }
    }
}
