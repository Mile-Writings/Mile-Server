package com.mile.comment.service;

import com.mile.comment.repository.CommentRepository;
import com.mile.commentreply.service.CommentReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentGetService {

    private final CommentRepository commentRepository;
    private final CommentReplyService commentReplyService;

    public int findCommentCountByWriterNameId(
            final Long writerNameId
    ) {
        return commentRepository.countByWriterNameId(writerNameId) + commentReplyService.countByWriterNameId(writerNameId);
    }
}
