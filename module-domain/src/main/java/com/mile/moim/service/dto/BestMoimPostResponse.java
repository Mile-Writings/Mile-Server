package com.mile.moim.service.dto;

import com.mile.post.domain.Post;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public record BestMoimPostResponse(String topicName, String imageUrl, String postTitle, String postContent) {
    private static final int SUBSTRING_START = 0;
    private static final int SUBSTRING_END = 200;

    public static BestMoimPostResponse of(Post post) {

        return new BestMoimPostResponse(
                post.getTopic().getContent(),
                post.getImageUrl(),
                post.getTitle(),
                getSubStringOfCleanContent(post.getContent())
        );
    }

    private static String getSubStringOfCleanContent(
            String content
    ) {
        return Jsoup.clean(content, Whitelist.none()).substring(SUBSTRING_START, SUBSTRING_END);
    }
}

/*
    private static String getSubString(final Post post) {
        return Jsoup.clean(post.getContent(), Whitelist.none()).substring(SUBSTRING_START, SUBSTRING_END);
 */

