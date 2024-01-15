package com.mile.post.service;

import com.mile.aws.utils.S3Service;
import com.mile.comment.service.CommentService;
import com.mile.curious.service.CuriousService;
import com.mile.curious.service.dto.CuriousInfoResponse;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.domain.Moim;
import com.mile.moim.service.MoimService;
import com.mile.moim.service.dto.ContentListResponse;
import com.mile.post.domain.Post;
import com.mile.post.repository.PostRepository;
import com.mile.post.service.dto.CommentCreateRequest;
import com.mile.post.service.dto.CommentListResponse;
import com.mile.post.service.dto.PostCreateRequest;
import com.mile.post.service.dto.PostCuriousResponse;
import com.mile.post.service.dto.PostGetResponse;
import com.mile.post.service.dto.PostPutRequest;
import com.mile.post.service.dto.TemporaryPostCreateRequest;
import com.mile.post.service.dto.TemporaryPostGetResponse;
import com.mile.post.service.dto.WriterAuthenticateResponse;
import com.mile.topic.domain.Topic;
import com.mile.topic.service.TopicService;
import com.mile.topic.service.dto.ContentWithIsSelectedResponse;
import com.mile.user.service.UserService;
import com.mile.writerName.domain.WriterName;
import com.mile.writerName.service.WriterNameService;

import java.util.Base64;
import java.util.List;

import com.mile.writerName.service.dto.WriterNameResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostAuthenticateService postAuthenticateService;
    private final CommentService commentService;
    private final WriterNameService writerNameService;
    private final CuriousService curiousService;
    private final UserService userService;
    private final TopicService topicService;
    private final S3Service s3Service;

    public static final boolean TEMPRORARY_FALSE = false;
    public static final boolean TEMPORARY_TRUE = true;
    public static final boolean CURIOUS_FALSE = false;
    public static final boolean CURIOUS_TRUE = true;

    @Transactional
    public void createCommentOnPost(
            final Long postId,
            final Long userId,
            final CommentCreateRequest commentCreateRequest

    ) {
        Post post = findById(postId);
        Long moimId = post.getTopic().getMoim().getId();
        postAuthenticateService.authenticateUserWithPost(post, userId);
        commentService.createComment(post, writerNameService.findByMoimAndUser(moimId, userId), commentCreateRequest);
    }


    @Transactional
    public PostCuriousResponse createCuriousOnPost(
            final Long postId,
            final Long userId
    ) {
        Post post = findById(postId);
        postAuthenticateService.authenticateUserWithPost(post, userId);
        curiousService.createCurious(post, userService.findById(userId));
        return PostCuriousResponse.of(CURIOUS_TRUE);
    }

    public CommentListResponse getComments(
            final Long postId,
            final Long userId
    ) {
        return CommentListResponse.of(commentService.getCommentResponse(postId, userId));
    }


    public Post findById(
            final Long postId
    ) {
        return postRepository.findById(postId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.POST_NOT_FOUND)
                );
    }

    @Transactional(readOnly = true)
    public CuriousInfoResponse getCuriousInfo(
            final Long postId,
            final Long userId
    ) {
        Post post = findById(postId);
        postAuthenticateService.authenticateUserWithPost(post, userId);
        return curiousService.getCuriousInfoResponse(post, userService.findById(userId));
    }

    @Transactional
    public PostCuriousResponse deleteCuriousOnPost(
            final Long postId,
            final Long userId
    ) {
        Post post = findById(postId);
        postAuthenticateService.authenticateUserWithPost(post, userId);
        curiousService.deleteCurious(post, userService.findById(userId));
        return PostCuriousResponse.of(CURIOUS_FALSE);
    }

    @Transactional
    public void updatePost(
            final Long postId,
            final Long userId,
            final PostPutRequest putRequest
    ) {
        Post post = findById(postId);
        postAuthenticateService.authenticateWriter(postId, userId);
        Topic topic = topicService.findById(decodeUrlToLong(putRequest.topicId()));
        update(post, topic, putRequest);
    }

    private void update(
            final Post post,
            final Topic topic,
            final PostPutRequest putRequest
    ) {
        post.updatePost(topic, putRequest);
    }

    public WriterAuthenticateResponse getAuthenticateWriter(
            final Long postId,
            final Long userId
    ) {
        return WriterAuthenticateResponse.of(postAuthenticateService.authenticateWriterWithPost(postId, userId));
    }

    @Transactional
    public void deletePost(
            final Long postId,
            final Long userId
    ) {
        postAuthenticateService.authenticateWriterWithPost(postId, userId);
        delete(postId);
    }

    private void delete(
            final Long postId
    ) {
        Post post = findById(postId);
        deleteRelatedData(post);
        postRepository.delete(post);
    }

    private void deleteRelatedData(
            final Post post
    ) {
        if (post.isContainPhoto()) {
            deleteS3File(post.getImageUrl());
        }
        curiousService.deleteAllByPost(post);
        commentService.deleteAllByPost(post);
    }

    private void deleteS3File(
            final String key
    ) {
        s3Service.deleteImage(key);
    }

    @Transactional(readOnly = true)
    public TemporaryPostGetResponse getTemporaryPost(
            final Long postId,
            final Long userId
    ) {
        Post post = findById(postId);
        postAuthenticateService.authenticateUserWithPost(post, userId);
        isPostTemporary(post);

        List<ContentWithIsSelectedResponse> contentResponse = topicService.getContentsWithIsSelectedFromMoim(post.getTopic().getMoim().getId(), post.getTopic().getId());
        return TemporaryPostGetResponse.of(post, contentResponse);
    }

    private void isPostTemporary(
            final Post post
    ) {
        if (!post.isTemporary()) {
            throw new BadRequestException(ErrorMessage.POST_NOT_TEMPORARY_ERROR);
        }
    }

    public PostGetResponse getPost(
            final Long postId
    ) {
        Post post = findById(postId);
        Moim moim = post.getTopic().getMoim();
        return PostGetResponse.of(post, moim);
    }


    private Long decodeUrlToLong(
            final String url
    ) {
        return Long.parseLong(new String(Base64.getUrlDecoder().decode(url)));
    }

    @Transactional
    public WriterNameResponse createPost(
            final Long userId,
            final PostCreateRequest postCreateRequest
    ) {
        postAuthenticateService.authenticateWriterOfMoim(userId, decodeUrlToLong(postCreateRequest.moimId()));
        WriterName writerName = writerNameService.findByMoimAndUser(decodeUrlToLong(postCreateRequest.moimId()), userId);
        Post post = createPost(postCreateRequest, writerName);
        postRepository.saveAndFlush(post);
        post.setIdUrl(Base64.getUrlEncoder().encodeToString(post.getId().toString().getBytes()));
        return WriterNameResponse.of(post.getIdUrl(), writerName.getName());
    }

    private Post createPost(final PostCreateRequest postCreateRequest, final WriterName writerName) {
        return Post.create(
                topicService.findById(decodeUrlToLong(postCreateRequest.topicId())), // Topic
                writerName, // WriterName
                postCreateRequest.title(),
                postCreateRequest.content(),
                postCreateRequest.imageUrl(),
                checkContainPhoto(postCreateRequest.imageUrl()),
                postCreateRequest.anonymous(),
                TEMPRORARY_FALSE
        );
    }

    public void createTemporaryPost(
            final Long userId,
            final TemporaryPostCreateRequest temporaryPostCreateRequest
    ) {
        postAuthenticateService.authenticateWriterOfMoim(userId, decodeUrlToLong(temporaryPostCreateRequest.moimId()));
        Post post = postRepository.saveAndFlush(Post.create(
                topicService.findById(decodeUrlToLong(temporaryPostCreateRequest.topicId())), // Topic
                writerNameService.findByMoimAndUser(decodeUrlToLong(temporaryPostCreateRequest.moimId()), userId), // WriterName
                temporaryPostCreateRequest.title(),
                temporaryPostCreateRequest.content(),
                temporaryPostCreateRequest.imageUrl(),
                checkContainPhoto(temporaryPostCreateRequest.imageUrl()),
                temporaryPostCreateRequest.anonymous(),
                TEMPORARY_TRUE
        ));
        post.setIdUrl(Base64.getUrlEncoder().encodeToString(post.getId().toString().getBytes()));
    }

    private boolean checkContainPhoto(String imageUrl) {
        return imageUrl != null && !imageUrl.isEmpty();
    }


    @Transactional
    public WriterNameResponse putFixedPost(final Long userId, final PostPutRequest request, final Long postId) {
        postAuthenticateService.authenticateWriterWithPost(postId, userId);
        Post post = findById(postId);
        isPostTemporary(post);
        post.updatePost(topicService.findById(decodeUrlToLong(request.topicId())), request);
        return WriterNameResponse.of(post.getIdUrl(), post.getWriterName().getName());
    }
}
