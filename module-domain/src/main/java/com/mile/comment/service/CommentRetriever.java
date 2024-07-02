package com.mile.comment.service;

import com.mile.comment.domain.Comment;
import com.mile.comment.repository.CommentRepository;
import com.mile.commentreply.service.CommentReplyRetriever;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.ForbiddenException;
import com.mile.exception.model.NotFoundException;
import com.mile.post.domain.Post;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentRetriever {

    private final CommentRepository commentRepository;
    private final CommentReplyRetriever commentReplyRetriever;

    public int findCommentCountByWriterNameId(
            final Long writerNameId
    ) {
        return commentRepository.countByWriterNameId(writerNameId) + commentReplyRetriever.countByWriterNameId(writerNameId);
    }

    public List<Comment> findByPostId(
            final Long postId
    ) {
        return commentRepository.findByPostId(postId);
    }

    public boolean isCommentWriterEqualWriterOfPost(
            final Comment comment,
            final Post post
    ) {
        return post.getWriterName().equals(comment.getWriterName());
    }

    public Long getMoimIdFromComment(final Comment comment) {
        return comment.getPost().getTopic().getMoim().getId();
    }

    public void authenticateUser(
            final Comment comment,
            final Long userId
    ) {
        if (!commentRepository.findUserIdByComment(comment).equals(userId)) {
            throw new ForbiddenException(ErrorMessage.COMMENT_ACCESS_ERROR);
        }
    }

    public Comment findById(
            final Long commentId
    ) {
        return commentRepository.findById(commentId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND)
                );
    }

    public int countByPost(
            final Post post
    ) {
        return commentRepository.countByPost(post);
    }

    public List<Comment> findAllByPosts(
            final List<Post> posts
    ) {
        return posts.stream()
                .flatMap(post -> findByPostId(post.getId()).stream())
                .collect(Collectors.toList());
    }

}
