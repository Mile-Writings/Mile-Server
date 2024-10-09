package com.mile.topic.service;

import com.mile.topic.domain.Topic;
import com.mile.topic.service.dto.request.TopicPutRequest;
import com.mile.user.domain.User;
import com.mile.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TopicUpdator {

    private final TopicRetriever topicRetriever;
    private final UserService userService;


    @Transactional
    public void putTopic(
            final Long topicId,
            final TopicPutRequest topicPutRequest
    ) {
        Topic topic = topicRetriever.findById(topicId);
        topic.updateTopic(topicPutRequest);
    }

}
