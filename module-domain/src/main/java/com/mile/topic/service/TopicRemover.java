package com.mile.topic.service;

import com.mile.moim.domain.Moim;
import com.mile.post.service.PostRemover;
import com.mile.post.service.PostRetriever;
import com.mile.topic.domain.Topic;
import com.mile.topic.repository.TopicRepository;
import com.mile.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TopicRemover {

    private final PostRetriever postGetService;
    private final PostRemover postDeleteService;
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
