package com.mile.topic.service;

import com.mile.comment.service.CommentService;
import com.mile.config.BaseTimeEntity;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.exception.model.ForbiddenException;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.domain.Moim;
import com.mile.moim.service.dto.MoimTopicInfoListResponse;
import com.mile.moim.service.dto.MoimTopicInfoResponse;
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
import com.mile.topic.service.dto.TopicPutRequest;
import com.mile.topic.service.dto.TopicResponse;
import com.mile.user.domain.User;
import com.mile.user.service.UserService;
import com.mile.utils.SecureUrlUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicService {

    private static final int TOPIC_PER_PAGE_SIZE = 4;

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
                .sorted(Comparator.comparing(BaseTimeEntity::getCreatedAt).reversed())
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
            final Long topicId,
            final String lastPostId
    ) {
        Topic topic = findById(topicId);
        Slice<Post> posts = postGetService.findByTopicAndLastPostId(topic, secureUrlUtil.decodeIfNotNull(lastPostId));
        return PostListInTopicResponse.of(TopicOfMoimResponse.of(topic),
                posts.stream().sorted(Comparator.comparing(BaseTimeEntity::getCreatedAt).reversed())
                        .map(p -> PostListResponse.of(p, commentService.countByPost(p))).toList(),
                posts.hasNext()
        );
    }


    public MoimTopicInfoListResponse getTopicListFromMoim(
            final Long moimId,
            final int page
    ) {

        PageRequest pageRequest = PageRequest.of(page-1, TOPIC_PER_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Topic> topicPage = topicRepository.findByMoimIdOrderByCreatedAtDesc(moimId, pageRequest);

        isContentsEmpty(topicPage.getContent());

        return getTopicResponsesFromPage(topicPage, moimId);
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

    public Long getNumberOfTopicFromMoim(
            final Long moimId
    ) {
        return topicRepository.countByMoimId(moimId);
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
    ){
        Topic topic = findById(topicId);
        User user = userService.findById(userId);
        authenticateTopicWithUser(topic, user);
        checkSingleTopicDeletion(topic);

        deletePostsOfTopic(topic);
        topicRepository.deleteById(topic.getId());
    }

    @Transactional
    public void putTopic(
            final Long userId,
            final Long topicId,
            final TopicPutRequest topicPutRequest
    ) {
        Topic topic = findById(topicId);
        User user = userService.findById(userId);
        authenticateTopicWithUser(topic, user);
        topic.updateTopic(topicPutRequest);
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
}

