package com.mile.post.service;

import com.mile.aws.utils.S3Service;
import com.mile.comment.service.CommentService;
import com.mile.curious.serivce.CuriousService;
import com.mile.curious.serivce.dto.CuriousInfoResponse;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import com.mile.post.repository.PostRepository;
import com.mile.post.service.dto.CommentCreateRequest;
import com.mile.post.service.dto.CommentListResponse;
import com.mile.post.service.dto.PostGetResponse;
import com.mile.post.service.dto.PostPutRequest;
import com.mile.post.service.dto.TemporaryPostCreateRequest;
import com.mile.post.service.dto.TemporaryPostGetResponse;
import com.mile.post.service.dto.WriterAuthenticateResponse;
import com.mile.topic.domain.Topic;
import com.mile.topic.serivce.TopicService;
import com.mile.user.service.UserService;
import com.mile.writerName.domain.WriterName;
import com.mile.writerName.serivce.WriterNameService;
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
    public void createCuriousOnPost(
            final Long postId,
            final Long userId
    ) {
        Post post = findById(postId);
        postAuthenticateService.authenticateUserWithPost(post, userId);
        curiousService.createCurious(post, userService.findById(userId));
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
    public void deleteCuriousOnPost(
            final Long postId,
            final Long userId
    ) {
        Post post = findById(postId);
        postAuthenticateService.authenticateUserWithPost(post, userId);
        curiousService.deleteCurious(post, userService.findById(userId));
    }

    @Transactional
    public void updatePost(
            final Long postId,
            final Long userId,
            final PostPutRequest putRequest
    ) {
        Post post = findById(postId);
        postAuthenticateService.authenticateWriter(postId, userId);
        Topic topic = topicService.findById(putRequest.topicId());
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
        return TemporaryPostGetResponse.of(post);
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

    public void createTemporaryPost(
            final Long userId,
            final TemporaryPostCreateRequest temporaryPostCreateRequest
    ) {
        postAuthenticateService.authenticateWriterOfMoim(userId, temporaryPostCreateRequest.moimId());
        postRepository.save(Post.create(
                topicService.findById(temporaryPostCreateRequest.topicId()), // Topic
                writerNameService.findByMoimAndUser(temporaryPostCreateRequest.moimId(), userId), // WriterName
                temporaryPostCreateRequest.title(),
                temporaryPostCreateRequest.content(),
                temporaryPostCreateRequest.imageUrl(),
                temporaryPostCreateRequest.anonymous(),
                true // isTemporary
        ));
    }



}
