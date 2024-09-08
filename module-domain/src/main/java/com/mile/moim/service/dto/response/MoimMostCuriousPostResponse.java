package com.mile.moim.service.dto.response;

import com.mile.moim.domain.popular.MoimCuriousPost;
import com.mile.common.utils.JsoupUtil;

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
            final MoimCuriousPost post
            ) {
        return new MoimMostCuriousPostResponse(post.getIdUrl(), post.getImgUrl(), post.getTopic(),
                post.getTitle(), getSubStringOfCleanContent(post.getContents()), post.isContainPhoto());
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
