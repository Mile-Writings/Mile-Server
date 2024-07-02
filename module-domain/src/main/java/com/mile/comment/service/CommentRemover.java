package com.mile.comment.service;

import com.mile.comment.domain.Comment;
import com.mile.comment.repository.CommentRepository;
import com.mile.commentreply.service.CommentReplyRemover;
import com.mile.post.domain.Post;

import java.util.List;

import com.mile.post.service.PostRetriever;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentRemover {

    private final CommentRepository commentRepository;
    private final CommentReplyRemover commentReplyRemover;
    private final PostRetriever postRetriever;
    private final CommentRetriever commentRetriever;

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


    public void deleteByPost(
            final Post post
    ) {
        commentRepository.deleteAllByPost(post);
    }

    public void deleteAllByPost(
            final Post post
    ) {
        commentRetriever.findByPostId(post.getId()).forEach(commentReplyRemover::deleteRepliesByComment);
        deleteByPost(post);
    }

    public void deleteComments(final List<Post> posts) {
        posts.forEach(this::deleteAllByPost);
    }


}
