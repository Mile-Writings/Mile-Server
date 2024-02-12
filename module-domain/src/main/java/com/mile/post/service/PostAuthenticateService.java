package com.mile.post.service;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.ForbiddenException;
import com.mile.exception.model.NotFoundException;
import com.mile.post.domain.Post;
import com.mile.post.repository.PostRepository;
import com.mile.writername.domain.WriterName;
import com.mile.writername.service.WriterNameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostAuthenticateService {
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
        authenticateUserOfMoim(moimId, userId);
    }

    public void authenticateUserOfMoim(
            final Long moimId,
            final Long userId
    ) {
        if (!writerNameService.isUserInMoim(moimId, userId)) {
            throw new ForbiddenException(ErrorMessage.USER_AUTHENTICATE_ERROR);
        }
    }

    private Post findById(
            final Long postId
    ) {
        return postRepository.findById(postId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.POST_NOT_FOUND)
                );
    }

    public boolean existsPostByWriterWithPost(
            final Long postId,
            final Long writerNameId
    ) {
        return postRepository.existsPostByIdAndWriterNameId(postId, writerNameId);
    }

    public void authenticateWriterWithPost(
            final Long postId,
            final Long writerNameId
    ) {
        if(!existsPostByWriterWithPost(postId, writerNameId)) {
            throw new ForbiddenException(ErrorMessage.WRITER_AUTHENTICATE_ERROR);
        }
    }

    public WriterName authenticateWriter(
            final Long postId,
            final Long userId
    ) {
        WriterName writerName = writerNameService.getWriterNameIdByPostAndUserId(findById(postId), userId);
        authenticateWriterWithPost(postId, writerName.getId());
        return writerName;
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
