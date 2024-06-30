package com.mile.comment.service;

import com.mile.comment.repository.CommentRepository;
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

}
