package com.mile.writername.service;

import com.mile.comment.service.CommentService;
import com.mile.curious.service.CuriousService;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.domain.Moim;
import com.mile.moim.service.MoimService;
import com.mile.post.service.PostDeleteService;
import com.mile.writername.domain.WriterName;
import com.mile.writername.repository.WriterNameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WriterNameDeleteService {

    private final WriterNameRepository writerNameRepository;
    private final PostDeleteService postDeleteService;
    private final CommentService commentService;
    private final CuriousService curiousService;
    private final MoimService moimService;

    @Transactional
    public void deleteWriterNameById(
            final Long writerNameId,
            final Long userId
    ) {
        WriterName writerName = findById(writerNameId);
        moimService.authenticateOwnerOfMoim(writerName.getMoim(), userId);

        postDeleteService.deleteAllPostByWriterNameId(writerNameId);
        commentService.deleteAllCommentByWriterNameId(writerNameId);
        curiousService.deleteAllByWriterNameId(writerNameId);

        writerNameRepository.delete(writerName);
    }

    private WriterName findById(
            final Long writerNameId
    ) {
        return writerNameRepository.findById(writerNameId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.WRITER_NOT_FOUND)
        );
    }

    public void deleteWriterNamesByMoim(
            final Moim moim
    ) {
        writerNameRepository.deleteAllByMoim(moim);
    }
}
