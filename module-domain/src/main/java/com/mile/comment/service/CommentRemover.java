package com.mile.comment.service;

import com.mile.comment.domain.Comment;
import com.mile.comment.repository.CommentRepository;
import com.mile.commentreply.service.CommentReplyRemover;
import com.mile.post.domain.Post;
import com.mile.writername.domain.WriterName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentRemover {

    private final CommentRepository commentRepository;
    private final CommentReplyRemover commentReplyRemover;
    private final CommentRetriever commentRetriever;

    public void deleteAllCommentByWriterNameId(
            final WriterName writerName
    ) {
        commentRepository.deleteAllByWriterName(writerName);
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
