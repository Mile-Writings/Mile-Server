package com.mile.comment.service;

import com.mile.comment.domain.Comment;
import com.mile.comment.repository.CommentRepository;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.ForbiddenException;
import com.mile.exception.model.NotFoundException;
import com.mile.comment.service.dto.CommentResponse;
import com.mile.post.domain.Post;
import com.mile.post.service.PostAuthenticateService;
import com.mile.post.service.dto.CommentCreateRequest;
import com.mile.writerName.domain.WriterName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private static boolean ANONYMOUS_TRUE = true;
    private final PostAuthenticateService postAuthenticateService;
    private final CommentRepository commentRepository;

    @Transactional
    public void deleteComment(
            final Long commentId,
            final Long userId
    ) {
        Comment comment = findById(commentId);
        authenticateUser(comment, userId);
        delete(comment);
    }

    private void delete(
            final Comment comment
    ) {
        commentRepository.delete(comment);
    }

    private Comment findById(
            final Long commentId
    ) {
        return commentRepository.findById(commentId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND)
                );
    }
    private void authenticateUser(
            final Comment comment,
            final Long userId
    ) {
        if(!commentRepository.findUserIdByComment(comment).equals(userId))  {
            throw new ForbiddenException(ErrorMessage.COMMENT_ACCESS_ERROR);
        }
    }
    public void createComment(
            final Post post,
            final WriterName writerName,
            final CommentCreateRequest commentCreateRequest
    ) {
        commentRepository.save(Comment.create(post, writerName, commentCreateRequest, ANONYMOUS_TRUE));
    }

    public List<CommentResponse> getCommentResponse(
            final Long postId,
            final Long userId
    ) {
        postAuthenticateService.authenticateUserWithPostId(postId, userId);
        List<Comment> commentList = findByPostId(postId);
        throwIfCommentIsNull(commentList);
        return commentList.stream()
                .map(comment -> CommentResponse.of(comment, userId)).collect(Collectors.toList());
    }

    private List<Comment> findByPostId(
            final Long postId
    ) {
        return commentRepository.findByPostId(postId);
    }

    private void throwIfCommentIsNull(
            final List<Comment> commentList
    ) {
        if (isCommentListNull(commentList)) {
            throw new NotFoundException(ErrorMessage.COMMENTS_NOT_FOUND);
        }
    }

    private boolean isCommentListNull(
            final List<Comment> commentList
    ) {
        return commentList.isEmpty();
    }
}
