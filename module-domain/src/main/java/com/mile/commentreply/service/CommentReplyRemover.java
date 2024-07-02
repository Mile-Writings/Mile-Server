package com.mile.commentreply.service;

import com.mile.comment.domain.Comment;
import com.mile.commentreply.domain.CommentReply;
import com.mile.commentreply.repository.CommentReplyRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentReplyRemover {

    private final CommentReplyRepository commentReplyRepository;

    public void deleteCommentReply(
            final CommentReply commentReply
    ) {
        commentReplyRepository.delete(commentReply);
    }

    public void deleteRepliesByComment(
            final Comment comment
    ) {
        commentReplyRepository.deleteCommentRepliesByComment(comment);
    }

    public void deleteRepliesByComments(final List<Comment> comments) {
        comments.forEach(this::deleteRepliesByComment);
    }
}
