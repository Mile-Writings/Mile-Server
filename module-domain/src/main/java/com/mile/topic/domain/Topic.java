package com.mile.topic.domain;

import com.mile.config.BaseTimeEntity;
import com.mile.moim.domain.Moim;
import com.mile.moim.service.dto.TopicCreateRequest;
import com.mile.topic.service.dto.TopicPutRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@NoArgsConstructor
public class Topic extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Moim moim;
    @Setter
    private String idUrl;
    private String keyword;
    private String content;
    private String description;

    @Builder
    private Topic(final Moim moim,
                  final String content,
                  final String keyword,
                  final String description) {
        this.moim = moim;
        this.content = content;
        this.keyword = keyword;
        this.description = description;
    }

    public static Topic create(
            final Moim moim,
            final TopicCreateRequest topicCreateRequest
    ) {
        return Topic.builder()
                .moim(moim)
                .content(topicCreateRequest.topicName())
                .keyword(topicCreateRequest.topicTag())
                .description(topicCreateRequest.topicDescription())
                .build();
    }

    public void updateTopic(
            final TopicPutRequest putRequest
    ) {
        this.content = putRequest.topic();
        this.keyword = putRequest.topicTag();
        this.description = putRequest.topicDescription();
    }

}
