package com.mile.comment.service;

import com.mile.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentGetService {

    private final CommentRepository commentRepository;

    public int findCommentCountByWriterNameId(
            final Long writerNameId
    ) {
        return commentRepository.countByWriterNameId(writerNameId);
    }
}
