package com.mile.comment.service;

import com.mile.comment.domain.Comment;
import com.mile.comment.repository.CommentRepository;
import com.mile.post.domain.Post;
import com.mile.post.service.dto.request.CommentCreateRequest;
import com.mile.utils.SecureUrlUtil;
import com.mile.writername.domain.WriterName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentCreator {

    private final CommentRepository commentRepository;
    private final SecureUrlUtil secureUrlUtil;

    private Comment create(
            final Post post,
            final WriterName writerName,
            final CommentCreateRequest commentCreateRequest
    ) {
        return commentRepository.save(Comment.create(post, writerName, commentCreateRequest));
    }

    public void createComment(
            final Post post,
            final WriterName writerName,
            final CommentCreateRequest commentCreateRequest
    ) {
        Comment comment = create(post, writerName, commentCreateRequest);
        comment.setIdUrl(secureUrlUtil.encodeUrl(comment.getId()));
        commentRepository.save(comment);
    }

}
