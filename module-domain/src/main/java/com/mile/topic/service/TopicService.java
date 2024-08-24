package com.mile.topic.service;

import com.mile.topic.domain.Topic;
import com.mile.topic.service.dto.response.ContentWithIsSelectedResponse;
import com.mile.topic.service.dto.response.PostListInTopicResponse;
import com.mile.topic.service.dto.response.TopicDetailResponse;
import com.mile.topic.service.dto.request.TopicPutRequest;
import com.mile.user.domain.User;
import com.mile.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRetriever topicRetriever;
    private final TopicRemover topicRemover;
    private final TopicUpdator topicUpdator;
    private final TopicCreator topicCreator;
    private final UserService userService;

    public List<ContentWithIsSelectedResponse> getContentsWithIsSelectedFromMoim(
            final Long moimId,
            final Long selectedTopicId
    ) {
        return topicRetriever.getContentsWithIsSelectedFromMoim(moimId, selectedTopicId);
    }

    public PostListInTopicResponse getPostListByTopic(
            final Long topicId,
            final String lastPostId
    ) {
        return topicRetriever.getPostListByTopic(topicId, lastPostId);
    }

    public TopicDetailResponse getTopicDetail(
            final Long userId,
            final Long topicId
    ) {
        return topicRetriever.getTopicDetail(userId, topicId);
    }

    @Transactional
    public void deleteTopic(
            final Long userId,
            final Long topicId
    ) {
        Topic topic = topicRetriever.findById(topicId);
        User user = userService.findById(userId);
        topicRetriever.authenticateTopicWithUser(topic, user);
        topicRetriever.checkSingleTopicDeletion(topic);
        topicRemover.deleteTopic(topic);
    }

    public void putTopic(
            final Long userId,
            final Long topicId,
            final TopicPutRequest topicPutRequest
    ) {
        topicUpdator.putTopic(userId, topicId, topicPutRequest);
    }

}

