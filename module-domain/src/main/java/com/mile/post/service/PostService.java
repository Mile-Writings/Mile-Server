package com.mile.post.service;

import com.mile.aws.utils.S3Service;
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
import com.mile.topic.service.TopicService;
import com.mile.topic.service.dto.ContentWithIsSelectedResponse;
import com.mile.user.service.UserService;
import com.mile.utils.SecureUrlUtil;
import com.mile.writername.domain.WriterName;
import com.mile.writername.service.WriterNameService;
import com.mile.writername.service.dto.WriterNameResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostUpdateService postUpdateService;
    private final PostGetService postGetService;
    private final PostAuthenticateService postAuthenticateService;
    private final CommentService commentService;
    private final WriterNameService writerNameService;
    private final CuriousService curiousService;
    private final UserService userService;
    private final TopicService topicService;
    private final S3Service s3Service;
    private final SecureUrlUtil secureUrlUtil;

    private static final boolean TEMPRORARY_FALSE = false;
    private static final boolean TEMPORARY_TRUE = true;
    private static final boolean CURIOUS_FALSE = false;
    private static final boolean CURIOUS_TRUE = true;
    private static final String DEFAULT_IMG_URL = "https://mile-s3.s3.ap-northeast-2.amazonaws.com/post/KakaoTalk_Photo_2024-01-14-15-52-49.png";

    @Transactional
    public void createCommentOnPost(
            final Long postId,
            final Long userId,
            final CommentCreateRequest commentCreateRequest

    ) {
        Post post = postGetService.findById(postId);
        Long moimId = post.getTopic().getMoim().getId();
        postAuthenticateService.authenticateUserWithPost(post, userId);
        commentService.createComment(post, writerNameService.findByMoimAndUser(moimId, userId), commentCreateRequest);
    }


    @Transactional
    public PostCuriousResponse createCuriousOnPost(
            final Long postId,
            final Long userId
    ) {
        Post post = postGetService.findById(postId);
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



    @Transactional(readOnly = true)
    public CuriousInfoResponse getCuriousInfo(
            final Long postId,
            final Long userId
    ) {
        Post post = postGetService.findById(postId);
        postAuthenticateService.authenticateUserWithPost(post, userId);
        return curiousService.getCuriousInfoResponse(post, userService.findById(userId));
    }

    @Transactional
    public PostCuriousResponse deleteCuriousOnPost(
            final Long postId,
            final Long userId
    ) {
        Post post = postGetService.findById(postId);
        postAuthenticateService.authenticateUserWithPost(post, userId);
        curiousService.deleteCurious(post, userService.findById(userId));
        return PostCuriousResponse.of(CURIOUS_FALSE);
    }

    public void updatePost(
            final Long postId,
            final Long userId,
            final PostPutRequest putRequest
    ) {
        Post post = postGetService.findById(postId);
        postAuthenticateService.authenticateWriter(postId, userId);
        Topic topic = topicService.findById(decodeUrlToLong(putRequest.topicId()));
        postUpdateService.update(post, topic, putRequest);
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
        postAuthenticateService.authenticateWriter(postId, userId);
        delete(postId);
    }

    private void delete(
            final Long postId
    ) {
        Post post = postGetService.findById(postId);
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
        Post post = postGetService.findById(postId);
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
        Post post = postGetService.findById(postId);
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
        postAuthenticateService.authenticateUserOfMoim(decodeUrlToLong(postCreateRequest.moimId()), userId);
        WriterName writerName = writerNameService.findByMoimAndUser(decodeUrlToLong(postCreateRequest.moimId()), userId);
        Post post = createPost(postCreateRequest, writerName);
        postRepository.saveAndFlush(post);
        post.setIdUrl(secureUrlUtil.encodeUrl(post.getId()));
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

    @Transactional
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

    private boolean checkContainPhoto(final String imageUrl) {
        return !imageUrl.equals(DEFAULT_IMG_URL);
    }


    @Transactional
    public WriterNameResponse putTemporaryToFixedPost(final Long userId, final PostPutRequest request, final Long postId) {
        postAuthenticateService.authenticateWriter(postId, userId);
        Post post = postGetService.findById(postId);
        isPostTemporary(post);
        updatePost(postId, userId, request);
        post.setTemporary(false);
        return WriterNameResponse.of(post.getIdUrl(), post.getWriterName().getName());
    }

    @Transactional(readOnly = true)
    public ModifyPostGetResponse getPostForModifying(
            final Long postId,
            final Long userId
    ) {
        Post post = postGetService.findById(postId);
        postAuthenticateService.authenticateUserWithPost(post, userId);
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
