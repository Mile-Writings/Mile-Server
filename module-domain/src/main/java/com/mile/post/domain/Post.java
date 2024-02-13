package com.mile.post.domain;

import com.mile.config.BaseTimeEntity;
import com.mile.post.service.dto.PostPutRequest;
import com.mile.topic.domain.Topic;
import com.mile.writername.domain.WriterName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Topic topic;
    @ManyToOne(fetch = FetchType.LAZY)
    private WriterName writerName;
    private String title;
    @Column(length = 50000)
    private String content;
    private String imageUrl;
    private String idUrl;
    private int curiousCount;
    private boolean containPhoto;
    private boolean anonymous;
    private boolean isTemporary;

    public void increaseCuriousCount() {
        this.curiousCount++;
    }

    public void decreaseCuriousCount() {
        this.curiousCount--;
        setCuriousCountZero();
    }

    private void setCuriousCountZero() {
        if (this.curiousCount < 0) {
            this.curiousCount = 0;
        }
    }

    public static Post create(
            final Topic topic,
            final WriterName writerName,
            final String title,
            final String content,
            final String imageUrl,
            final boolean containPhoto,
            final boolean anonymous,
            final boolean isTemporary
    ) {
        return Post
                .builder()
                .topic(topic)
                .writerName(writerName)
                .title(title)
                .content(content)
                .imageUrl(returnImageUrl(imageUrl, containPhoto))
                .curiousCount(0)
                .containPhoto(containPhoto)
                .anonymous(anonymous)
                .isTemporary(isTemporary)
                .build();
    }


    public void updatePost(
            final Topic topic,
            final PostPutRequest putRequest
    ) {
        this.topic = topic;
        this.title = putRequest.title();
        this.content = putRequest.content();
        this.imageUrl = putRequest.imageUrl();
        this.anonymous = putRequest.anonymous();
    }

    public void setTemporary(
            final boolean isTemporary
    ) {
        this.isTemporary = isTemporary;
    }
    public void setIdUrl(
            final String idUrl
    ) {
        this.idUrl = idUrl;
    }

    private static String returnImageUrl(
            final String imageUrl,
            final boolean containPhoto
    ) {
        final String DEFAULT_IMAGE = "https://mile-s3.s3.ap-northeast-2.amazonaws.com/post/KakaoTalk_Photo_2024-01-14-15-52-49.png";
        if (!containPhoto) {
            return DEFAULT_IMAGE;
        } else {
            return imageUrl;
        }
    }
}
