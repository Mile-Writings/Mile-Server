package com.mile.post.service;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.ForbiddenException;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.service.MoimService;
import com.mile.post.domain.Post;
import com.mile.post.repository.PostRepository;
import com.mile.writerName.domain.WriterName;
import com.mile.writerName.service.WriterNameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostAuthenticateService {
    private final MoimService moimService;
    private final PostRepository postRepository;
    private final WriterNameService writerNameService;

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

    public void authenticateWriter(
            final Long postId,
            final Long userId
    ) {
        if (!authenticateWriterWithPost(postId, userId)) {
            throw new ForbiddenException(ErrorMessage.WRITER_AUTHENTICATE_ERROR);
        }
    }

    public void authenticateWriterOfMoim(
            final Long userId,
            final Long moimId
    ) {
        if (!writerNameService.isUserInMoim(moimId, userId)) {
            throw new ForbiddenException(ErrorMessage.WRITER_AUTHENTICATE_ERROR);
        }
    }
}
