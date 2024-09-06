package com.mile.moim.service.dto.response;

import com.mile.post.domain.Post;
import com.mile.common.utils.JsoupUtil;

public record BestMoimPostResponse(String postId, String topicName, String imageUrl, String postTitle,
                                   String postContent, Boolean isContainPhoto) {
    private static final int SUBSTRING_START = 0;
    private static final int SUBSTRING_END = 400;

    public static BestMoimPostResponse of(Post post) {

        return new BestMoimPostResponse(
                post.getIdUrl(),
                post.getTopic().getContent(),
                post.getImageUrl(),
                post.getTitle(),
                getSubStringOfCleanContent(post.getContent()),
                post.isContainPhoto()
        );
    }

    private static String getSubStringOfCleanContent(
            String content
    ) {
        String cleanContent = JsoupUtil.toPlainText(content);
        if (cleanContent.length() >= SUBSTRING_END) {
            return cleanContent.substring(SUBSTRING_START, SUBSTRING_END);
        } else {
            return cleanContent;
        }
    }
}
