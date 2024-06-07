package com.mile.commentreply.service;

import com.mile.commentreply.repository.CommentReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentReplyRetriever {

    private final CommentReplyRepository commentReplyRepository;

    public int countByWriterNameId(final Long writerNameId) {
        return commentReplyRepository.countByWriterNameId(writerNameId);
    }
}
