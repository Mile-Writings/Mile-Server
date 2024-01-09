package com.mile.post.service;

import com.mile.comment.service.CommentService;
import com.mile.curious.serivce.CuriousService;
import com.mile.curious.serivce.dto.CuriousInfoResponse;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;
import com.mile.post.domain.Post;
import com.mile.post.repository.PostRepository;
import com.mile.post.service.dto.CommentCreateRequest;
import com.mile.user.service.UserService;
import com.mile.post.service.dto.CommentListResponse;
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

}
