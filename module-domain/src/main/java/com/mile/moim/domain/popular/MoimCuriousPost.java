package com.mile.moim.domain.popular;

import com.mile.post.domain.Post;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MoimCuriousPost {
    private String title;
    private String idUrl;
    private String contents;
    private String imgUrl;
    private String topic;
    private boolean isContainPhoto;
    public static MoimCuriousPost of(final Post post) {
        return new MoimCuriousPost(
                post.getTitle(),
                post.getIdUrl(),
                post.getContent(),
                post.getImageUrl(),
                post.getTopic().getContent(),
                post.isContainPhoto()
        );
    }
}
