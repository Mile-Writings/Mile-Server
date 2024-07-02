package com.mile.comment.service;

import com.mile.comment.domain.Comment;
import com.mile.comment.repository.CommentRepository;
import com.mile.post.domain.Post;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentRemover {

    private final CommentRepository commentRepository;

    public void deleteAllCommentByWriterNameId(
            final Long writerNameId
    ) {
        commentRepository.deleteAllByWriterNameId(writerNameId);
    }

    public void delete(
            final Comment comment
    ) {
        commentRepository.delete(comment);
    }

    public void deleteAllByPost(
            final Post post
    ) {
        commentRepository.deleteAllByPost(post);
    }

    public void deleteComments(final List<Post> posts) {
        posts.forEach(this::deleteAllByPost);
    }


}