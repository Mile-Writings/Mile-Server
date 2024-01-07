package com.mile.post.service;

import com.mile.comment.service.CommentService;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.serivce.MoimService;
import com.mile.post.domain.Post;
import com.mile.post.repository.PostRepository;
import com.mile.post.service.dto.CommentCreateRequest;
import com.mile.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MoimService moimService;
    private final CommentService commentService;
    private final UserService userService;

    @Transactional
    public void createCommentOnPost(
            final Long postId,
            final Long userId,
            final CommentCreateRequest commentCreateRequest

    ) {
        Post post = findById(postId);
        authenticateUserWithPost(post, userId);
        commentService.createComment(post, userService.findById(userId), commentCreateRequest);
    }


    private void authenticateUserWithPost(
            final Post post,
            final Long userId
    ) {
        moimService.authenticateUserOfMoim(post.getTopic().getMoim().getId(), userId);
    }

    public Post findById(
            final Long postId
    ) {
        return postRepository.findById(postId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.POST_NOT_FOUND)
                );
    }
}
