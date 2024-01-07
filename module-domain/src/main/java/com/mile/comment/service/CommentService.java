package com.mile.comment.service;

import com.mile.comment.domain.Comment;
import com.mile.comment.repository.CommentRepository;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.ForbiddenException;
import com.mile.exception.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

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
}
