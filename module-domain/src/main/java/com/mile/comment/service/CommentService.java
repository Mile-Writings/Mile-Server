package com.mile.comment.service;

import com.mile.comment.domain.Comment;
import com.mile.comment.repository.CommentRepository;
import com.mile.comment.service.dto.CommentResponse;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;
import com.mile.post.domain.Post;
import com.mile.post.service.PostAuthenticateService;
import com.mile.post.service.dto.CommentCreateRequest;
import com.mile.user.domain.User;
import com.mile.writerName.domain.WriterName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private static boolean ANONYMOUS_TRUE = true;
    private final CommentRepository commentRepository;
    private final PostAuthenticateService postAuthenticateService;

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
        List<Comment> commentList = findById(postId);
        throwIfCommentIsNull(commentList);
        return commentList.stream()
                .map(comment -> CommentResponse.of(comment, userId)).collect(Collectors.toList());
    }

    private List<Comment> findById(
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
