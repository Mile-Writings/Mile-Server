package com.mile.post.domain;

import com.mile.config.BaseTimeEntity;
import com.mile.post.service.dto.PostPutRequest;
import com.mile.topic.domain.Topic;
import com.mile.writerName.domain.WriterName;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Topic topic;
    @ManyToOne
    private WriterName writerName;
    private String title;
    private String content;
    private String imageUrl;
    @ManyToOne
    private WriterName writerName;
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
