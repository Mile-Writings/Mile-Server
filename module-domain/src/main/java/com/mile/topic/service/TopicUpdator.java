package com.mile.topic.service;

import com.mile.topic.domain.Topic;
import com.mile.topic.service.dto.TopicPutRequest;
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
            final Long userId,
            final Long topicId,
            final TopicPutRequest topicPutRequest
    ) {
        Topic topic = topicRetriever.findById(topicId);
        User user = userService.findById(userId);
        topicRetriever.authenticateTopicWithUser(topic, user);
        topic.updateTopic(topicPutRequest);
    }

}
