package com.mile.post.domain;

import com.mile.config.BaseTimeEntity;
import com.mile.post.service.dto.PostPutRequest;
import com.mile.topic.domain.Topic;
import com.mile.writerName.domain.WriterName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    @ManyToOne
    private Topic topic;
    @ManyToOne
    private WriterName writerName;
    private String title;
    @Column(length = 50000)
    private String content;
    private String imageUrl;
    private int curiousCount;
    private boolean containPhoto;
    private boolean anonymous;
    private boolean isTemporary;

    public void increaseCuriousCount() {
        this.curiousCount++;
    }

    public void decreaseCuriousCount() {
        this.curiousCount--;
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
                .imageUrl(imageUrl)
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

}
