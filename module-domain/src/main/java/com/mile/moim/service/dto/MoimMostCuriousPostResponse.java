package com.mile.moim.service.dto;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public record MoimMostCuriousPostResponse(
        Long postId,
        String imageUrl,
        String topic,
        String title,
        String content
) {
    private static final int SUBSTRING_START = 0;
    private static final int SUBSTRING_END = 200;

    public static MoimMostCuriousPostResponse of(
            Long postId,
            String imageUrl,
            String topic,
            String title,
            String content
    ) {
        return new MoimMostCuriousPostResponse(postId, imageUrl, topic, title, getSubStringOfCleanContent(content));
    }

    private static String getSubStringOfCleanContent(
            String content
    ) {
        return Jsoup.clean(content, Whitelist.none()).substring(SUBSTRING_START, SUBSTRING_END);
    }
}
