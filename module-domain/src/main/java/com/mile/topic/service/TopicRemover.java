package com.mile.topic.service;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.moim.domain.Moim;
import com.mile.post.service.PostDeleteService;
import com.mile.post.service.PostGetService;
import com.mile.topic.domain.Topic;
import com.mile.topic.repository.TopicRepository;
import com.mile.user.domain.User;
import com.mile.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TopicRemover {

    private final TopicRetriever topicRetriever;
    private final UserService userService;
    private final PostGetService postGetService;
    private final PostDeleteService postDeleteService;
    private final TopicRepository topicRepository;

    @Transactional
    public void deleteTopic(
            final Long userId,
            final Long topicId
    ) {
        Topic topic = topicRetriever.findById(topicId);
        User user = userService.findById(userId);
        topicRetriever.authenticateTopicWithUser(topic, user);
        checkSingleTopicDeletion(topic);

        deletePostsOfTopic(topic);
        topicRepository.deleteById(topic.getId());
    }

    private void checkSingleTopicDeletion(
            final Topic topic
    ) {
        if (topicRepository.countByMoimId(topic.getMoim().getId()) <= 1) {
            throw new BadRequestException(ErrorMessage.LEAST_TOPIC_SIZE_OF_MOIM_ERROR);
        }
    }

    private void deletePostsOfTopic(
            final Topic topic
    ) {
        postGetService.findAllByTopic(topic)
                .forEach(postDeleteService::delete);
    }

    public void deleteTopicsByMoim(
            final Moim moim
    ) {
        topicRepository.deleteByMoim(moim);
    }

}
