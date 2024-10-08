package com.mile.post.service.dto.response;

import com.mile.post.domain.Post;
import com.mile.common.utils.DateUtil;
import com.mile.common.utils.JsoupUtil;

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
    private static final String UNNAMED = "작자미상";
    public static PostListResponse of(final Post post, final int commentCount) {

        return new PostListResponse(post.getIdUrl(), post.getTitle(), getSubString(post),
                getWriterName(post),
                DateUtil.getStringWithTimeOfLocalDate(post.getCreatedAt()),
                post.getCuriousCount(),
                post.getHitsCount(),
                commentCount,
                post.getImageUrl(),
                post.isContainPhoto());
    }

    private static String getWriterName(final Post post) {
        if(post.isAnonymous()) return UNNAMED;
        else return post.getWriterName().getName();
    }
    
    private static String getSubString(final Post post) {
        String cleanContent = JsoupUtil.toPlainText(post.getContent());
        if (cleanContent.length() >= SUBSTRING_END) {
            return cleanContent.substring(SUBSTRING_START, SUBSTRING_END);
        } else {
            return cleanContent;
        }
    }
}
