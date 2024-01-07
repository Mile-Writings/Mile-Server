package com.mile.comment.service;

import com.mile.comment.domain.Comment;
import com.mile.comment.repository.CommentRepository;
import com.mile.post.domain.Post;
import com.mile.post.service.dto.CommentCreateRequest;
import com.mile.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private static boolean ANONYMOUS_TRUE = true;
    private final CommentRepository commentRepository;

    public void createComment(
            final Post post,
            final User user,
            final CommentCreateRequest commentCreateRequest
    ) {
        commentRepository.save(Comment.create(post, user, commentCreateRequest, ANONYMOUS_TRUE));
    }
}
