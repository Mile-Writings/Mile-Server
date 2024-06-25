package com.mile.topic.service;

import com.mile.moim.domain.Moim;
import com.mile.moim.service.dto.MoimTopicInfoListResponse;
import com.mile.moim.service.dto.TopicCreateRequest;
import com.mile.topic.domain.Topic;
import com.mile.topic.service.dto.ContentResponse;
import com.mile.topic.service.dto.ContentWithIsSelectedResponse;
import com.mile.topic.service.dto.PostListInTopicResponse;
import com.mile.topic.service.dto.TopicDetailResponse;
import com.mile.topic.service.dto.TopicPutRequest;
import com.mile.topic.service.dto.TopicResponse;
import com.mile.topic.service.dto.TopicUpdator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRetriever topicRetriever;
    private final TopicRemover topicRemover;
    private final TopicUpdator topicUpdator;
    private final TopicCreator topicCreator;

    public List<ContentResponse> getContentsFromMoim(
            final Long moimId
    ) {
        return topicRetriever.getContentsFromMoim(moimId);
    }

    public List<ContentWithIsSelectedResponse> getContentsWithIsSelectedFromMoim(
            final Long moimId,
            final Long selectedTopicId
    ) {
        return topicRetriever.getContentsWithIsSelectedFromMoim(moimId, selectedTopicId);
    }

    public List<Topic> findTopicListByMoimId(
            final Long moimId
    ) {
        return topicRetriever.findTopicListByMoimId(moimId);
    }

    public List<TopicResponse> getKeywordsFromMoim(
            final Long moimId
    ) {
        return topicRetriever.getKeywordsFromMoim(moimId);
    }

    public String findLatestTopicByMoim(
            final Moim moim
    ) {
        return topicRetriever.findLatestTopicByMoim(moim);
    }

    public PostListInTopicResponse getPostListByTopic(
            final Long topicId,
            final String lastPostId
    ) {
        return topicRetriever.getPostListByTopic(topicId, lastPostId);
    }

    public MoimTopicInfoListResponse getTopicListFromMoim(
            final Long moimId,
            final int page
    ) {
        return topicRetriever.getTopicListFromMoim(moimId, page);
    }

    public MoimTopicInfoListResponse getTopicResponsesFromPage(Page<Topic> topicPage, final Long moimId) {
        return topicRetriever.getTopicResponsesFromPage(topicPage, moimId);
    }

    public Long getNumberOfTopicFromMoim(
            final Long moimId
    ) {
        return topicRetriever.getNumberOfTopicFromMoim(moimId);
    }

    public TopicDetailResponse getTopicDetail(
            final Long userId,
            final Long topicId
    ) {
        return topicRetriever.getTopicDetail(userId, topicId);
    }

    public Long createTopicOfMoim(
            final Moim moim,
            final TopicCreateRequest createRequest
    ) {
        return topicCreator.createTopicOfMoim(moim, createRequest);
    }

    public void deleteTopic(
            final Long userId,
            final Long topicId
    ) {
        topicRemover.deleteTopic(userId, topicId);
    }

    public void putTopic(
            final Long userId,
            final Long topicId,
            final TopicPutRequest topicPutRequest
    ) {
        topicUpdator.putTopic(userId, topicId, topicPutRequest);
    }

    public void deleteTopicsByMoim(
            final Moim moim
    ) {
        topicRemover.deleteTopicsByMoim(moim);
    }

}

