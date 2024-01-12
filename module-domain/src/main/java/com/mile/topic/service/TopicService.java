package com.mile.topic.service;

import com.mile.config.BaseTimeEntity;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.domain.Moim;
import com.mile.post.service.PostGetService;
import com.mile.post.service.dto.PostListResponse;
import com.mile.topic.domain.Topic;
import com.mile.topic.repository.TopicRepository;
import com.mile.topic.service.dto.ContentResponse;
import com.mile.topic.service.dto.PostListInTopicResponse;
import com.mile.topic.service.dto.TopicOfMoimResponse;
import com.mile.topic.service.dto.TopicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final PostGetService postGetService;

    public List<ContentResponse> getContentsFromMoim(
            final Long moimId
    ) {
        List<Topic> topicList = sortByCreatedAt(findByMoimId(moimId));
        isContentsEmpty(topicList);
        return topicList
                .stream()
                .map(ContentResponse::of)
                .collect(Collectors.toList());
    }

    private void isContentsEmpty(
            final List<Topic> topicList
    ) {
        if (topicList.isEmpty()) {
            throw new NotFoundException(ErrorMessage.CONTENT_NOT_FOUND);
        }
    }

    private List<Topic> findByMoimId(
            final Long moimId
    ) {
        return topicRepository.findByMoimId(moimId);
    }

    private List<Topic> sortByCreatedAt(
            final List<Topic> topicList
    ) {
        return topicList.stream()
                .sorted(Comparator.comparing(BaseTimeEntity::getCreatedAt))
                .collect(Collectors.toList());
    }

    public Topic findById(
            final Long topicId
    ) {
        return topicRepository.findById(topicId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.TOPIC_NOT_FOUND)
                );
    }

    public List<TopicResponse> getKeywordsFromMoim(
            final Long moimId
    ) {
        List<Topic> topicList = findByMoimId(moimId);
        isKeywordsEmpty(topicList);
        return topicList
                .stream()
                .map(TopicResponse::of)
                .collect(Collectors.toList());
    }

    private void isKeywordsEmpty(
            final List<Topic> topicList
    ) {
        if (topicList.isEmpty()) {
            throw new NotFoundException(ErrorMessage.KEYWORD_NOT_FOUND);
        }
    }

    public String findLatestTopicByMoim(
            final Moim moim
    ) {
        return topicRepository.findLatestTopicByMoim(moim)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.MOIM_TOPIC_NOT_FOUND)
                );
    }

    public PostListInTopicResponse getPostListByTopic(
            final Long topicId
    ) {
        Topic topic = findById(topicId);
        return PostListInTopicResponse.of(TopicOfMoimResponse.of(topic),
                postGetService.findByTopic(topic).stream().map(PostListResponse::of).collect(Collectors.toList()));
    }
}
