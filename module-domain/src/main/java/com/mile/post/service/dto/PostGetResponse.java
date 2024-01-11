package com.mile.post.service.dto;

import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import com.mile.utils.DateUtil;
import com.mile.writerName.domain.WriterName;

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
    public static PostGetResponse of(Post post, Moim moim) {
        return new PostGetResponse(
                post.getTopic().getContent(),
                DateUtil.getKoreanStringOfLocalDate(post.getCreatedAt()),
                post.getTitle(),
                post.getContent(),
                post.getImageUrl(),
                post.getWriterName().getName(),
                moim.getName(),
                post.getWriterName().getInformation()
        );
    }
}
