package com.mile.topic.service;

import com.mile.comment.service.CommentService;
import com.mile.config.BaseTimeEntity;
import com.mile.curious.service.CuriousService;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.exception.model.ForbiddenException;
import com.mile.exception.model.MileException;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.domain.Moim;
import com.mile.moim.service.dto.TopicCreateRequest;
import com.mile.post.domain.Post;
import com.mile.post.service.PostDeleteService;
import com.mile.post.service.PostGetService;
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
import com.mile.user.service.UserService;
import com.mile.utils.SecureUrlUtil;
import jakarta.transaction.Transactional;
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
    private final UserService userService;
    private final PostGetService postGetService;
    private final SecureUrlUtil secureUrlUtil;
    private final PostDeleteService postDeleteService;

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

    private void authenticateTopicWithUser(
            final Topic topic,
            final User user
    ) {
        if(!topic.getMoim().getOwner().getWriter().equals(user)){
            throw new ForbiddenException(ErrorMessage.MOIM_OWNER_AUTHENTICATION_ERROR);
        }
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

    public TopicDetailResponse getTopicDetail(
            final Long userId,
            final Long topicId
    ) {
        Topic topic = findById(topicId);
        authenticateTopicWithUser(topic, userService.findById(userId));
        return TopicDetailResponse.of(topic);
    }

    @Transactional
    public Long createTopicOfMoim(
            final Moim moim,
            final TopicCreateRequest createRequest
    ) {
        Topic topic = topicRepository.saveAndFlush(Topic.create(moim, createRequest));
        topic.setIdUrl(secureUrlUtil.encodeUrl(topic.getId()));
        return topic.getId();
    }

    @Transactional
    public void deleteTopic(
            final Long userId,
            final Long topicId
    ) {
        Topic topic = findById(topicId);
        User user = userService.findById(userId);
        authenticateTopicWithUser(topic, user);

        preventSingleTopicDeletion(topic);

        deletePostsOfTopic(topic);
        topicRepository.deleteById(topic.getId());
    }

    private void preventSingleTopicDeletion(
            final Topic topic
    ) {
        Long topicCount = topicRepository.countByMoimId(topic.getMoim().getId());
        if (topicCount <= 1) {
            throw new BadRequestException(ErrorMessage.LEAST_TOPIC_SIZE_OF_MOIM_ERROR);
        }
    }

    private void deletePostsOfTopic(
        final Topic topic
    ) {
        List<Post> posts = postGetService.findAllByTopic(topic);
        for (Post post: posts) {
            postDeleteService.delete(post);
        }
    }
}
