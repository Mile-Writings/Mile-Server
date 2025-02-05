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
import com.mile.post.service.dto.request.CommentCreateRequest;
import com.mile.post.service.dto.response.CommentListResponse;
import com.mile.post.service.dto.response.ModifyPostGetResponse;
import com.mile.post.service.dto.request.PostCreateRequest;
import com.mile.post.service.dto.response.PostCuriousResponse;
import com.mile.post.service.dto.response.PostDataResponse;
import com.mile.post.service.dto.response.PostGetResponse;
import com.mile.post.service.dto.request.PostPutRequest;
import com.mile.post.service.dto.request.TemporaryPostCreateRequest;
import com.mile.post.service.dto.response.TemporaryPostGetResponse;
import com.mile.post.service.dto.response.PostAuthenticateResponse;
import com.mile.topic.domain.Topic;
import com.mile.topic.service.TopicRetriever;
import com.mile.topic.service.TopicService;
import com.mile.topic.service.dto.response.ContentWithIsSelectedResponse;
import com.mile.common.utils.SecureUrlUtil;
import com.mile.util.MoimWriterNameMapUtil;
import com.mile.writername.domain.WriterName;
import com.mile.writername.service.WriterNameRetriever;
import com.mile.writername.service.dto.response.WriterNameResponse;
import com.mile.writername.service.vo.WriterNameInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


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
            final HashMap<Long, WriterNameInfo> moimWriterInfoMap,
            final CommentCreateRequest commentCreateRequest

    ) {
        Post post = postRetriever.findById(postId);
        final Long moimId = post.getTopic().getMoim().getId();
        final Long writerNameId = MoimWriterNameMapUtil.getWriterNameIdMoimWriterNameMap(moimId, moimWriterInfoMap);
        commentCreator.createComment(post, writerNameRetriever.findByIdNonException(writerNameId), commentCreateRequest);
    }

    @Transactional
    public PostCuriousResponse createCuriousOnPost(
            final Long postId,
            final HashMap<Long, WriterNameInfo> moimWriterNameMap
    ) {
        Post post = postRetriever.findById(postId);
        Long moimId = post.getTopic().getMoim().getId();
        final Long writerNameId = MoimWriterNameMapUtil.getWriterNameIdMoimWriterNameMap(moimId, moimWriterNameMap);
        curiousService.createCurious(post, writerNameRetriever.findById(writerNameId));
        return PostCuriousResponse.of(CURIOUS_TRUE);
    }

    public CommentListResponse getComments(
            final Long postId,
            final HashMap<Long, WriterNameInfo> moimWriterNameMap
    ) {
        Post post = postRetriever.findById(postId);
        final Long writerNameId = MoimWriterNameMapUtil.getWriterNameIdMoimWriterNameMap(
                post.getTopic().getMoim().getId(),
                moimWriterNameMap
        );
        return CommentListResponse.of(commentService.getCommentResponse(post, writerNameId));
    }

    @Transactional(readOnly = true)
    public CuriousInfoResponse getCuriousInfoOfPost(
            final Long postId,
            final HashMap<Long, WriterNameInfo> moimWriterNameMap
    ) {
        Post post = postRetriever.findById(postId);
        final Long writerNameId = MoimWriterNameMapUtil.getWriterNameIdMoimWriterNameMap(
                post.getTopic().getMoim().getId(),
                moimWriterNameMap
        );
        return curiousService.getCuriousInfoOfPostAndWriterName(post, writerNameRetriever.findById(writerNameId));
    }

    @Transactional
    public PostCuriousResponse deleteCuriousOnPost(
            final Long postId,
            final HashMap<Long, WriterNameInfo> moimWriterNameMap
    ) {
        Post post = postRetriever.findById(postId);
        final Long writerNameId = MoimWriterNameMapUtil.getWriterNameIdMoimWriterNameMap(
                post.getTopic().getMoim().getId(),
                moimWriterNameMap);
        curiousService.deleteCurious(post, writerNameRetriever.findByIdNonException(writerNameId));
        return PostCuriousResponse.of(CURIOUS_FALSE);
    }

    public void updatePost(
            final Long postId,
            final HashMap<Long, WriterNameInfo> moimWriteNameMap,
            final PostPutRequest putRequest
    ) {
        Post post = postRetriever.findById(postId);
        final Long moimId = post.getTopic().getMoim().getId();
        final Long writerNameId = MoimWriterNameMapUtil.getWriterNameIdMoimWriterNameMap(moimId, moimWriteNameMap);
        postRetriever.authenticateWriterWithPost(postId, writerNameId);
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
        if (!postRetriever.isWriterOfPost(post, writerName) && !moimRetriever.isMoimOwnerEqualsUser(post.getTopic().getMoim(), userId)) {
            throw new ForbiddenException(ErrorMessage.WRITER_AUTHENTICATE_ERROR);
        }
        postRemover.delete(post);
    }

    @Transactional(readOnly = true)
    public TemporaryPostGetResponse getTemporaryPost(
            final Long postId,
            final HashMap<Long, WriterNameInfo> moimWriterNameMap
    ) {
        Post post = postRetriever.findById(postId);
        Topic selectedTopic = post.getTopic();
        Moim moim = selectedTopic.getMoim();
        final Long writerNameId = MoimWriterNameMapUtil.getWriterNameIdMoimWriterNameMap(moim.getId(), moimWriterNameMap);
        postRetriever.authenticateWriterWithPost(postId, writerNameId);
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

    public void deleteTemporaryPost(final Long postId,
                                    final HashMap<Long, WriterNameInfo> moimWriterNameMap) {
        final Long moimId = getMoimIdFromPostId(postId);
        final Long writerNameId = MoimWriterNameMapUtil.getWriterNameIdMoimWriterNameMap(moimId, moimWriterNameMap);
        postRetriever.authenticateWriterWithPost(postId, writerNameId);
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

    /*
    FIX ME 아래 메서드는 리턴 값에 writerName의 name이 포함되어야 해서 Interceptor 인가가 필요 없을 듯 함
    * */
    @Transactional
    public WriterNameResponse putTemporaryToFixedPost(final Long userId, final PostPutRequest request, final Long postId) {
        Post post = postRetriever.findById(postId);
        WriterName writerName = writerNameRetriever.findByMoimAndUser(post.getTopic().getMoim().getId(), userId);
        postRetriever.authenticateWriterWithPost(postId, writerName.getId());
        isPostTemporary(post);
        postUpdator.updateTemporaryPost(post, post.getTopic(), request);
        return WriterNameResponse.of(post.getIdUrl(), writerName.getName());
    }

    public ModifyPostGetResponse getPostForModifying(
            final Long postId,
            final HashMap<Long, WriterNameInfo> moimWriterNameMap
    ) {
        Post post = postRetriever.findById(postId);
        final Long moimId = post.getTopic().getMoim().getId();
        final Long writerNameId = MoimWriterNameMapUtil.getWriterNameIdMoimWriterNameMap(moimId, moimWriterNameMap);
        postRetriever.authenticateWriterWithPost(post.getId(), writerNameId);
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
