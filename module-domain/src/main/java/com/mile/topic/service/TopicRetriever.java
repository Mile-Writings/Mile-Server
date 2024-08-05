package com.mile.topic.service;

import com.mile.comment.service.CommentRetriever;
import com.mile.comment.service.CommentService;
import com.mile.commentreply.service.CommentReplyRetriever;
import com.mile.config.BaseTimeEntity;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.exception.model.ForbiddenException;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.domain.Moim;
import com.mile.moim.service.dto.MoimTopicInfoListResponse;
import com.mile.moim.service.dto.MoimTopicInfoResponse;
import com.mile.post.domain.Post;
import com.mile.post.service.PostRetriever;
import com.mile.post.service.dto.PostListResponse;
import com.mile.topic.domain.Topic;
import com.mile.topic.repository.TopicRepository;
import com.mile.topic.service.dto.ContentResponse;
import com.mile.topic.service.dto.ContentWithIsSelectedResponse;
import com.mile.topic.service.dto.PostListInTopicResponse;
import com.mile.topic.service.dto.TopicDetailResponse;
import com.mile.topic.service.dto.TopicOfMoimResponse;
import com.mile.topic.service.dto.TopicResponse;
import com.mile.user.domain.User;
import com.mile.user.service.UserRetriever;
import com.mile.utils.SecureUrlUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TopicRetriever {

    private static final int TOPIC_PER_PAGE_SIZE = 4;

    private final TopicRepository topicRepository;
    private final PostRetriever postRetriever;
    private final SecureUrlUtil secureUrlUtil;
    private final CommentRetriever commentRetriever;
    private final UserRetriever userRetriever;
    private final CommentReplyRetriever commentReplyRetriever;


    public void authenticateTopicWithUser(
            final Topic topic,
            final User user
    ) {
        if (!topic.getMoim().getOwner().getWriter().equals(user)) {
            throw new ForbiddenException(ErrorMessage.MOIM_OWNER_AUTHENTICATION_ERROR);
        }
    }

    public Topic findById(
            final Long topicId
    ) {
        return topicRepository.findById(topicId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.TOPIC_NOT_FOUND)
                );
    }

    public List<Topic> sortByCreatedAt(
            final List<Topic> topicList
    ) {
        return topicList.stream()
                .sorted(Comparator.comparing(BaseTimeEntity::getCreatedAt).reversed())
                .toList();
    }

    public PostListInTopicResponse getPostListByTopic(
            final Long topicId,
            final String lastPostId
    ) {
        Topic topic = findById(topicId);
        Slice<Post> posts = postRetriever.findByTopicAndLastPostId(topic, secureUrlUtil.decodeIfNotNull(lastPostId));
        return PostListInTopicResponse.of(TopicOfMoimResponse.of(topic),
                posts.stream().sorted(Comparator.comparing(BaseTimeEntity::getCreatedAt).reversed())
                        .map(p -> PostListResponse.of(p, commentRetriever.countByPost(p) + commentReplyRetriever.countByPost(p))).toList(),
                posts.hasNext()
        );
    }

    public TopicDetailResponse getTopicDetail(
            final Long userId,
            final Long topicId
    ) {
        Topic topic = findById(topicId);
        authenticateTopicWithUser(topic, userRetriever.findById(userId));
        return TopicDetailResponse.of(topic);
    }

    public Long getNumberOfTopicFromMoim(
            final Long moimId
    ) {
        return topicRepository.countByMoimId(moimId);
    }

    public MoimTopicInfoListResponse getTopicResponsesFromPage(Page<Topic> topicPage, final Long moimId) {
        List<MoimTopicInfoResponse> infoResponses = topicPage.getContent()
                .stream()
                .map(MoimTopicInfoResponse::of)
                .collect(Collectors.toList());

        return MoimTopicInfoListResponse.of(
                topicPage.getTotalPages(),
                getNumberOfTopicFromMoim(moimId),
                infoResponses
        );
    }

    public MoimTopicInfoListResponse getTopicListFromMoim(
            final Long moimId,
            final int page
    ) {

        PageRequest pageRequest = PageRequest.of(page - 1, TOPIC_PER_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Topic> topicPage = topicRepository.findByMoimIdOrderByCreatedAtDesc(moimId, pageRequest);

        isContentsEmpty(topicPage.getContent());

        return getTopicResponsesFromPage(topicPage, moimId);
    }

    private void isContentsEmpty(
            final List<Topic> topicList
    ) {
        if (topicList.isEmpty()) {
            throw new NotFoundException(ErrorMessage.CONTENT_NOT_FOUND);
        }
    }

    private void checkTopicListEmpty(
            final List<Topic> topicList
    ) {
        if (topicList.isEmpty()) {
            throw new NotFoundException(ErrorMessage.KEYWORD_NOT_FOUND);
        }
    }

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

    public List<Topic> findTopicListByMoimId(
            final Long moimId
    ) {
        return topicRepository.findByMoimId(moimId);
    }


    public List<Topic> findTopicListByMoim(
            final Moim moim
    ) {
        return topicRepository.findByMoim(moim);
    }

    public List<TopicResponse> getKeywordsFromMoim(
            final Long moimId
    ) {
        List<Topic> topicList = findTopicListByMoimId(moimId);
        checkTopicListEmpty(topicList);
        return topicList
                .stream()
                .sorted(Comparator.comparing(BaseTimeEntity::getCreatedAt).reversed())
                .map(TopicResponse::of)
                .collect(Collectors.toList());
    }

    public String findLatestTopicByMoim(
            final Moim moim
    ) {
        return topicRepository.findLatestTopicByMoim(moim)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.MOIM_TOPIC_NOT_FOUND)
                );
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

    public void checkSingleTopicDeletion(
            final Topic topic
    ) {
        if (topicRepository.countByMoimId(topic.getMoim().getId()) <= 1) {
            throw new BadRequestException(ErrorMessage.LEAST_TOPIC_SIZE_OF_MOIM_ERROR);
        }
    }
}
