package com.mile.post.service;

import com.mile.comment.service.CommentService;
import com.mile.curious.service.CuriousService;
import com.mile.curious.service.dto.CuriousInfoResponse;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import com.mile.post.repository.PostRepository;
import com.mile.post.service.dto.CommentCreateRequest;
import com.mile.post.service.dto.CommentListResponse;
import com.mile.post.service.dto.ModifyPostGetResponse;
import com.mile.post.service.dto.PostCreateRequest;
import com.mile.post.service.dto.PostCuriousResponse;
import com.mile.post.service.dto.PostGetResponse;
import com.mile.post.service.dto.PostPutRequest;
import com.mile.post.service.dto.TemporaryPostCreateRequest;
import com.mile.post.service.dto.TemporaryPostGetResponse;
import com.mile.post.service.dto.WriterAuthenticateResponse;
import com.mile.topic.domain.Topic;
import com.mile.topic.service.TopicRetriever;
import com.mile.topic.service.TopicService;
import com.mile.topic.service.dto.ContentWithIsSelectedResponse;
import com.mile.utils.SecureUrlUtil;
import com.mile.writername.domain.WriterName;
import com.mile.writername.service.WriterNameRetriever;
import com.mile.writername.service.dto.WriterNameResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostUpdator postUpdateService;
    private final PostRetriever postRetriever;
    private final CommentService commentService;
    private final WriterNameRetriever writerNameRetriever;
    private final CuriousService curiousService;
    private final TopicService topicService;
    private final TopicRetriever topicRetriever;
    private final PostRemover postDeleteService;
    private final SecureUrlUtil secureUrlUtil;
    private final PostCreator postCreator;

    private static final boolean TEMPORARY_FALSE = false;
    private static final boolean CURIOUS_FALSE = false;
    private static final boolean CURIOUS_TRUE = true;
    private static final String DEFAULT_IMG_URL = "https://mile-s3.s3.ap-northeast-2.amazonaws.com/test/groupMile.png";

    @Transactional
    public void createCommentOnPost(
            final Long postId,
            final Long userId,
            final CommentCreateRequest commentCreateRequest

    ) {
        Post post = postRetriever.findById(postId);
        Long moimId = post.getTopic().getMoim().getId();
        postRetriever.authenticateUserOfMoim(writerNameRetriever.isUserInMoim(moimId, userId));
        commentService.createComment(post, writerNameRetriever.findByMoimAndUser(moimId, userId), commentCreateRequest);
    }

    @Transactional
    public PostCuriousResponse createCuriousOnPost(
            final Long postId,
            final Long userId
    ) {
        Post post = postRetriever.findById(postId);
        Long moimId = post.getTopic().getMoim().getId();
        postRetriever.authenticateUserOfMoim(writerNameRetriever.isUserInMoim(moimId, userId));
        curiousService.createCurious(post, writerNameRetriever.findByMoimAndUser(moimId, userId));
        return PostCuriousResponse.of(CURIOUS_TRUE);
    }

    public CommentListResponse getComments(
            final Long postId,
            final Long userId
    ) {
        return CommentListResponse.of(commentService.getCommentResponse(getMoimIdByPostId(postId), postId, userId));
    }

    @Transactional(readOnly = true)
    public CuriousInfoResponse getCuriousInfoOfPost(
            final Long postId,
            final Long userId
    ) {
        Post post = postRetriever.findById(postId);
        postRetriever.authenticateUserWithPost(post, userId);
        return curiousService.getCuriousInfoOfPostAndWriterName(post, writerNameRetriever.findByMoimAndUser(post.getTopic().getMoim().getId(), userId));
    }

    @Transactional
    public PostCuriousResponse deleteCuriousOnPost(
            final Long postId,
            final Long userId
    ) {
        Post post = postRetriever.findById(postId);
        curiousService.deleteCurious(post, writerNameRetriever.findByMoimAndUser(post.getTopic().getMoim().getId(), userId));
        return PostCuriousResponse.of(CURIOUS_FALSE);
    }

    public void updatePost(
            final Long postId,
            final Long userId,
            final PostPutRequest putRequest
    ) {
        Post post = postRetriever.findById(postId);
        postRetriever.authenticateWriter(postId,
                writerNameRetriever.findWriterNameByMoimIdAndUserId(post.getTopic().getMoim().getId(), userId));
        Topic topic = topicRetriever.findById(decodeUrlToLong(putRequest.topicId()));
        postUpdateService.update(post, topic, putRequest);
    }

    private void updateTemporaryPost(
            final Long postId,
            final PostPutRequest putRequest
    ) {
        Post post = postRetriever.findById(postId);
        Topic topic = topicRetriever.findById(decodeUrlToLong(putRequest.topicId()));
        postUpdateService.update(post, topic, putRequest);
    }

    public WriterAuthenticateResponse getAuthenticateWriter(
            final Long postId,
            final Long userId
    ) {
        return WriterAuthenticateResponse.of(postRetriever.existsPostByWriterWithPost(postId,
                writerNameRetriever.getWriterNameByPostAndUserId(postRetriever.findById(postId), userId).getId()));
    }

    @Transactional
    public void deletePost(
            final Long postId,
            final Long userId
    ) {
        Post post = postRetriever.findById(postId);
        postRetriever.authenticateWriter(postId,
                writerNameRetriever.findWriterNameByMoimIdAndUserId(post.getTopic().getMoim().getId(), userId));
        postDeleteService.delete(post);
    }

    @Transactional(readOnly = true)
    public TemporaryPostGetResponse getTemporaryPost(
            final Long postId,
            final Long userId
    ) {
        Post post = postRetriever.findById(postId);
        Topic selectedTopic = post.getTopic();
        Moim moim = selectedTopic.getMoim();
        postRetriever.authenticateUserWithPost(post, userId);
        isPostTemporary(post);
        List<ContentWithIsSelectedResponse> contentResponse = topicService.getContentsWithIsSelectedFromMoim(moim.getId(), selectedTopic.getId());
        return TemporaryPostGetResponse.of(post, contentResponse);
    }

    private void isPostTemporary(
            final Post post
    ) {
        if (!post.isTemporary()) {
            throw new BadRequestException(ErrorMessage.POST_NOT_TEMPORARY_ERROR);
        }
    }

    @Transactional
    public PostGetResponse getPost(
            final Long postId
    ) {
        Post post = postRetriever.findById(postId);
        post.increaseHits();
        Moim moim = post.getTopic().getMoim();
        return PostGetResponse.of(post, moim, commentService.countByPost(post));
    }


    private Long decodeUrlToLong(
            final String url
    ) {
        return Long.parseLong(new String(Base64.getUrlDecoder().decode(url)));
    }

    public void deleteTemporaryPost(final Long userId, final Long postId) {
        postRetriever.authenticateWriter(postId, userId);
        Post post = postRetriever.findById(postId);
        postDeleteService.deleteTemporaryPost(post);
    }


    @Transactional
    public WriterNameResponse createPost(
            final Long userId,
            final PostCreateRequest postCreateRequest
    ) {
        postRetriever.authenticateUserOfMoim(writerNameRetriever.isUserInMoim(decodeUrlToLong(postCreateRequest.moimId()), userId));
        WriterName writerName = writerNameRetriever.findByMoimAndUser(decodeUrlToLong(postCreateRequest.moimId()), userId);
        Post post = createPost(postCreateRequest, writerName);
        postRepository.saveAndFlush(post);
        post.setIdUrl(secureUrlUtil.encodeUrl(post.getId()));
        return WriterNameResponse.of(post.getIdUrl(), writerName.getName());
    }

    private Post createPost(final PostCreateRequest postCreateRequest, final WriterName writerName) {
        return Post.create(
                topicRetriever.findById(decodeUrlToLong(postCreateRequest.topicId())), // Topic
                writerName, // WriterName
                postCreateRequest.title(),
                postCreateRequest.content(),
                postCreateRequest.imageUrl(),
                checkContainPhoto(postCreateRequest.imageUrl()),
                postCreateRequest.anonymous(),
                TEMPORARY_FALSE
        );
    }

    @Transactional
    public void createTemporaryPost(
            final Long userId,
            final TemporaryPostCreateRequest temporaryPostCreateRequest
    ) {
        postRetriever.authenticateWriterOfMoim(userId, decodeUrlToLong(temporaryPostCreateRequest.moimId()));
        WriterName writerName = writerNameRetriever.findByMoimAndUser(secureUrlUtil.decodeUrl(temporaryPostCreateRequest.moimId()), userId);
        postDeleteService.deleteTemporaryPosts(topicRetriever.findById(secureUrlUtil.decodeUrl(temporaryPostCreateRequest.topicId())).getMoim(), writerName);
        postCreator.createTemporaryPost(writerName, temporaryPostCreateRequest);
    }

    private boolean checkContainPhoto(final String imageUrl) {
        return !imageUrl.equals(DEFAULT_IMG_URL);
    }

    @Transactional
    public WriterNameResponse putTemporaryToFixedPost(final Long userId, final PostPutRequest request, final Long postId) {
        WriterName writerName = postRetriever.authenticateWriter(postId, userId);
        Post post = postRetriever.findById(postId);
        isPostTemporary(post);
        updateTemporaryPost(postId, request);
        post.setTemporary(TEMPORARY_FALSE);
        post.updateCratedAt(LocalDateTime.now());
        return WriterNameResponse.of(post.getIdUrl(), writerName.getName());
    }

    @Transactional(readOnly = true)
    public ModifyPostGetResponse getPostForModifying(
            final Long postId,
            final Long userId
    ) {
        Post post = postRetriever.findById(postId);
        postRetriever.authenticateUserWithPost(post, userId);
        isPostNotTemporary(post);
        List<ContentWithIsSelectedResponse> contentResponse = topicService.getContentsWithIsSelectedFromMoim(post.getTopic().getMoim().getId(), post.getTopic().getId());
        return ModifyPostGetResponse.of(post, contentResponse);
    }

    private void isPostNotTemporary(
            final Post post
    ) {
        if (post.isTemporary()) {
            throw new BadRequestException(ErrorMessage.POST_TEMPORARY_ERROR);
        }
    }
}
