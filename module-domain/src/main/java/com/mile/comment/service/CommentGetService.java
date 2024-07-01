package com.mile.comment.service;

import com.mile.comment.repository.CommentRepository;
import com.mile.commentreply.service.CommentReplyRetriever;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentGetService {

    private final CommentRepository commentRepository;
    private final CommentReplyRetriever commentReplyRetriever;

    public int findCommentCountByWriterNameId(
            final Long writerNameId
    ) {
        return commentRepository.countByWriterNameId(writerNameId) + commentReplyRetriever.countByWriterNameId(writerNameId);
    }
}
