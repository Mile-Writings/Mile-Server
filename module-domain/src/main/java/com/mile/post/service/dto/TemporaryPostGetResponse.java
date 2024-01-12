package com.mile.post.service.dto;

import com.mile.post.domain.Post;
import com.mile.topic.service.dto.ContentWithIsSelectedResponse;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;


public record TemporaryPostGetResponse(
        List<ContentWithIsSelectedResponse> topicList,
        String title,
        String content,
        String imageUrl,
        boolean anonymous
) {
    public static TemporaryPostGetResponse of(Post post, List<ContentWithIsSelectedResponse> contentResponse) {
        return new TemporaryPostGetResponse(
                contentResponse,
                post.getTitle(),
                getCleanContent(post.getContent()),
                post.getImageUrl(),
                post.isAnonymous()
        );
    }

    private static String getCleanContent(
            String content
    ) {
        return Jsoup.clean(content, Whitelist.none());
    }

}