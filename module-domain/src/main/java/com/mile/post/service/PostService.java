package com.mile.post.service;

import com.mile.comment.service.CommentCreator;
import com.mile.comment.service.CommentRetriever;
import com.mile.comment.service.CommentService;
import com.mile.commentreply.service.CommentReplyRetriever;
import com.mile.curious.service.CuriousService;
import com.mile.curious.service.dto.CuriousInfoResponse;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.exception.model.ForbiddenException;
import com.mile.moim.domain.Moim;
import com.mile.moim.service.MoimRetriever;
import com.mile.post.domain.Post;
import com.mile.post.service.dto.CommentCreateRequest;
import com.mile.post.service.dto.CommentListResponse;
import com.mile.post.service.dto.ModifyPostGetResponse;
import com.mile.post.service.dto.PostCreateRequest;
import com.mile.post.service.dto.PostCuriousResponse;
import com.mile.post.service.dto.PostGetResponse;
import com.mile.post.service.dto.PostPutRequest;
import com.mile.post.service.dto.TemporaryPostCreateRequest;
import com.mile.post.service.dto.TemporaryPostGetResponse;
import com.mile.post.service.dto.PostAuthenticateResponse;
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

import java.util.Base64;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostUpdator postUpdator;
    private final PostRetriever postRetriever;
    private final CommentCreator commentCreator;
    private final WriterNameRetriever writerNameRetriever;
    private final CuriousService curiousService;
    private final TopicService topicService;
    private final TopicRetriever topicRetriever;
    private final PostRemover postRemover;
    private final SecureUrlUtil secureUrlUtil;
    private final PostCreator postCreator;
    private final CommentService commentService;
    private final CommentRetriever commentRetriever;
    private final MoimRetriever moimRetriever;
    private final CommentReplyRetriever commentReplyRetriever;

    private static final boolean CURIOUS_FALSE = false;
    private static final boolean CURIOUS_TRUE = true;
    private static final String ROLE_WRITER = "writer";
    private static final String ROLE_ANONYMOUS = "anonymous";
    private static final String ROLE_MEMBER = "member";
    private static final String ROLE_OWNER = "owner";

    @Transactional
    public void createCommentOnPost(
            final Long postId,
            final Long userId,
            final CommentCreateRequest commentCreateRequest

    ) {
        Post post = postRetriever.findById(postId);
        final Long moimId = post.getTopic().getMoim().getId();
        postRetriever.authenticateUserWithPost(post, userId);
        commentCreator.createComment(post, writerNameRetriever.findByMoimAndUser(moimId, userId), commentCreateRequest);
    }

    @Transactional
    public PostCuriousResponse createCuriousOnPost(
            final Long postId,
            final Long userId
    ) {
        Post post = postRetriever.findById(postId);
        Long moimId = post.getTopic().getMoim().getId();
        postRetriever.authenticateUserWithPost(post, userId);
        curiousService.createCurious(post, writerNameRetriever.findByMoimAndUser(moimId, userId));
        return PostCuriousResponse.of(CURIOUS_TRUE);
    }

    public CommentListResponse getComments(
            final Long postId,
            final Long userId
    ) {
        Post post = postRetriever.findById(postId);
        return CommentListResponse.of(commentService.getCommentResponse(post.getTopic().getMoim().getId(), post, userId));
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
        curiousService.deleteCurious(post, writerNameRetriever.findByMoimAndUserWithNotExceptionCase(post.getTopic().getMoim().getId(), userId));
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
        postUpdator.update(post, topic, putRequest);
    }


    public PostAuthenticateResponse getAuthenticateWriter(
            final Long postId,
            final Long userId
    ) {
        Post post = postRetriever.findById(postId);
        Moim moim = post.getTopic().getMoim();
        WriterName postWriterName = post.getWriterName();

        if (!writerNameRetriever.isUserInMoim(moim.getId(), userId)) {
            return PostAuthenticateResponse.of(ROLE_ANONYMOUS);
        }

        WriterName userWriterName = writerNameRetriever.findByMoimAndUser(moim.getId(), userId);
        if (postWriterName.equals(userWriterName)) {
            return PostAuthenticateResponse.of(ROLE_WRITER);
        }

        if (moim.getOwner().equals(userWriterName)) {
            return PostAuthenticateResponse.of(ROLE_OWNER);
        }

        return PostAuthenticateResponse.of(ROLE_MEMBER);
    }

    @Transactional
    public void deletePost(
            final Long postId,
            final Long userId
    ) {
        Post post = postRetriever.findById(postId);
        Long moimId = post.getTopic().getMoim().getId();
        WriterName writerName = writerNameRetriever.findByMoimAndUser(moimId, userId);
        if(!postRetriever.isWriterOfPost(post, writerName) && !moimRetriever.isMoimOwnerEqualsUser(post.getTopic().getMoim(), userId)){
            throw new ForbiddenException(ErrorMessage.WRITER_AUTHENTICATE_ERROR);
        }
        postRemover.delete(post);
    }

    @Transactional(readOnly = true)
    public TemporaryPostGetResponse getTemporaryPost(
            final Long postId,
            final Long userId
    ) {
        Post post = postRetriever.findById(postId);
        Topic selectedTopic = post.getTopic();
        Moim moim = selectedTopic.getMoim();
        postRetriever.authenticateWriter(postId, writerNameRetriever.findByMoimAndUser(moim.getId(), userId));
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
        return PostGetResponse.of(post, moim, commentRetriever.countByPost(post) + commentReplyRetriever.countByPost(post));
    }

    private Long getMoimIdFromPostId(final Long postId) {
        return postRetriever.findById(postId).getTopic().getMoim().getId();
    }


    private Long decodeUrlToLong(
            final String url
    ) {
        return Long.parseLong(new String(Base64.getUrlDecoder().decode(url)));
    }

    public void deleteTemporaryPost(final Long userId, final Long postId) {
        Long moimId = getMoimIdFromPostId(postId);
        postRetriever.authenticateWriter(postId, writerNameRetriever.findByMoimAndUser(moimId, userId));
        Post post = postRetriever.findById(postId);
        postRemover.deleteTemporaryPost(post);
    }


    @Transactional
    public WriterNameResponse createPost(
            final Long userId,
            final PostCreateRequest postCreateRequest
    ) {
        WriterName writerName = writerNameRetriever.findByMoimAndUser(decodeUrlToLong(postCreateRequest.moimId()), userId);
        Topic topic = topicRetriever.findById(decodeUrlToLong(postCreateRequest.topicId()));
        String postIdUrl = postCreator.create(postCreateRequest, topic, writerName);
        return WriterNameResponse.of(postIdUrl, writerName.getName());
    }


    @Transactional
    public void createTemporaryPost(
            final Long userId,
            final TemporaryPostCreateRequest temporaryPostCreateRequest
    ) {
        WriterName writerName = writerNameRetriever.findByMoimAndUser(secureUrlUtil.decodeUrl(temporaryPostCreateRequest.moimId()), userId);
        postRemover.deleteTemporaryPosts(topicRetriever.findById(secureUrlUtil.decodeUrl(temporaryPostCreateRequest.topicId())).getMoim(), writerName);
        Topic topic = topicRetriever.findById(decodeUrlToLong(temporaryPostCreateRequest.topicId()));
        postCreator.createTemporaryPost(writerName, topic, temporaryPostCreateRequest);
    }

    @Transactional
    public WriterNameResponse putTemporaryToFixedPost(final Long userId, final PostPutRequest request, final Long postId) {
        Post post = postRetriever.findById(postId);
        WriterName writerName = writerNameRetriever.findByMoimAndUser(post.getTopic().getMoim().getId(), userId);
        postRetriever.authenticateWriter(postId, writerName);
        isPostTemporary(post);
        postUpdator.updateTemporaryPost(post, post.getTopic(), request);
        return WriterNameResponse.of(post.getIdUrl(), writerName.getName());
    }

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
