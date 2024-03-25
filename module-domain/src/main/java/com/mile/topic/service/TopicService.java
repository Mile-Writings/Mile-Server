package com.mile.topic.service;

import com.mile.comment.service.CommentService;
import com.mile.config.BaseTimeEntity;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.domain.Moim;
import com.mile.moim.service.dto.MoimTopicInfoResponse;
import com.mile.post.service.PostGetService;
import com.mile.post.service.dto.PostListResponse;
import com.mile.topic.domain.Topic;
import com.mile.topic.repository.TopicRepository;
import com.mile.topic.service.dto.ContentResponse;
import com.mile.topic.service.dto.ContentWithIsSelectedResponse;
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
    private final CommentService commentService;
    private final PostGetService postGetService;

    public List<ContentResponse> getContentsFromMoim(
            final Long moimId
    ) {
        List<Topic> topicList = sortByCreatedAt(findTopicListByMoimId(moimId));
        isContentsEmpty(topicList);
        return topicList
                .stream()
                .map(ContentResponse::of)
                .collect(Collectors.toList());
    }

    public List<ContentWithIsSelectedResponse> getContentsWithIsSelectedFromMoim(
            final Long moimId,
            final Long selectedTopicId
    ) {
        List<Topic> topicList = sortByCreatedAt(findTopicListByMoimId(moimId));
        isContentsEmpty(topicList);

        return topicList.stream()
                .map(topic -> ContentWithIsSelectedResponse.of(topic, topic.getId().equals(selectedTopicId)))
                .collect(Collectors.toList());
    }


    private void isContentsEmpty(
            final List<Topic> topicList
    ) {
        if (topicList.isEmpty()) {
            throw new NotFoundException(ErrorMessage.CONTENT_NOT_FOUND);
        }
    }

    private List<Topic> findTopicListByMoimId(
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
        List<Topic> topicList = findTopicListByMoimId(moimId);
        checkKeywordsEmpty(topicList);
        return topicList
                .stream()
                .sorted(Comparator.comparing(BaseTimeEntity::getCreatedAt).reversed())
                .map(TopicResponse::of)
                .collect(Collectors.toList());
    }

    private void checkKeywordsEmpty(
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
                postGetService.findByTopic(topic).stream().map(p -> PostListResponse.of(p, commentService.findCommentCountByPost(p))).collect(Collectors.toList()));
    }

    public List<MoimTopicInfoResponse> getTopicListFromMoim(
            final Long moimId
    ) {
        List<Topic> topicList = sortByCreatedAt(findTopicListByMoimId(moimId));
        isContentsEmpty(topicList);
        return topicList
                .stream()
                .map(MoimTopicInfoResponse::of)
                .collect(Collectors.toList());
    }
}
