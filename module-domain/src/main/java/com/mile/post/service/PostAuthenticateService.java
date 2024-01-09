package com.mile.post.service;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.serivce.MoimService;
import com.mile.post.domain.Post;
import com.mile.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostAuthenticateService {
    private final MoimService moimService;
    private final PostRepository postRepository;

    public void authenticateUserWithPostId(
            final Long postId,
            final Long userId
    ) {
        Post post = findById(postId);
        authenticateUserWithPost(post, userId);
    }

    public void authenticateUserWithPost(
            final Post post,
            final Long userId
    ) {
        Long moimId = post.getTopic().getMoim().getId();
        moimService.authenticateUserOfMoim(moimId, userId);
    }

    private Post findById(
            final Long postId
    ) {
        return postRepository.findById(postId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.POST_NOT_FOUND)
                );
    }

    public boolean authenticateWriterWithPost(
            final Long postId,
            final Long userId
    ) {
        return postRepository.existsPostByIdAndWriterNameId(postId, userId);
    }
}
