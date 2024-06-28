package com.mile.topic.service;

import com.mile.moim.domain.Moim;
import com.mile.post.service.PostDeleteService;
import com.mile.post.service.PostGetService;
import com.mile.topic.domain.Topic;
import com.mile.topic.repository.TopicRepository;
import com.mile.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TopicRemover {

    private final PostGetService postGetService;
    private final PostDeleteService postDeleteService;
    private final TopicRepository topicRepository;

    public void deleteTopic(
            final Topic topic,
            final User user
    ) {
        deletePostsOfTopic(topic);
        topicRepository.deleteById(topic.getId());
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
