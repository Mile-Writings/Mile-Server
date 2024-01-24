package com.mile.post.service.dto;

import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import com.mile.utils.DateUtil;

public record PostGetResponse(
        String topic,
        String createdAt,
        String title,
        String content,
        String imageUrl,
        String writerName,
        String moimName,
        String writerInfo
) {
    private final static String ANONYMOUS = "작자미상";
    private final static String ANONYMOUS_INFO = "익명으로 작성한 사용자입니다.";

    public static PostGetResponse of(Post post, Moim moim) {
        String writerName = post.getWriterName().getName();
        String information = post.getWriterName().getInformation();
        if (post.isAnonymous()) {
            writerName = ANONYMOUS;
            information = ANONYMOUS_INFO;
        }
        return new PostGetResponse(
                post.getTopic().getContent(),
                DateUtil.getKoreanStringOfLocalDate(post.getCreatedAt()),
                post.getTitle(),
                post.getContent(),
                post.getImageUrl(),
                writerName,
                moim.getName(),
                information
        );
    }
}
