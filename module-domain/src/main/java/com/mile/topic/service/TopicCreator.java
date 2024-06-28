package com.mile.topic.service;

import com.mile.moim.domain.Moim;
import com.mile.moim.service.dto.TopicCreateRequest;
import com.mile.topic.domain.Topic;
import com.mile.topic.repository.TopicRepository;
import com.mile.utils.SecureUrlUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TopicCreator {

    private final TopicRepository topicRepository;
    private final SecureUrlUtil secureUrlUtil;

    @Transactional
    public Long createTopicOfMoim(
            final Moim moim,
            final TopicCreateRequest createRequest
    ) {
        Topic topic = topicRepository.saveAndFlush(Topic.create(moim, createRequest));
        topic.setIdUrl(secureUrlUtil.encodeUrl(topic.getId()));
        return topic.getId();
    }
}
