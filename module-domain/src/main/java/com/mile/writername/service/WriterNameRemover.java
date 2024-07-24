package com.mile.writername.service;

import com.mile.comment.service.CommentRemover;
import com.mile.commentreply.service.CommentReplyRemover;
import com.mile.curious.service.CuriousRemover;
import com.mile.moim.domain.Moim;
import com.mile.post.service.PostRemover;
import com.mile.writername.domain.WriterName;
import com.mile.writername.repository.WriterNameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class WriterNameRemover {

    private final WriterNameRepository writerNameRepository;
    private final CommentReplyRemover commentReplyRemover;
    private final CommentRemover commentRemover;
    private final CuriousRemover curiousRemover;
    private final PostRemover postRemover;

    public void deleteWriterNamesByMoim(
            final Moim moim
    ) {
        writerNameRepository.deleteWritersExceptOwner(moim, moim.getOwner());
    }

    public void deleteRelatedData(final WriterName writerName) {
        commentReplyRemover.deleteRepliesByWriterName(writerName);
        commentRemover.deleteAllCommentByWriterNameId(writerName);
        curiousRemover.deleteAllByWriterName(writerName);
        postRemover.deleteAllPostByWriterNameId(writerName);
    }

    public void deleteWriterName(final WriterName writerName) {
        writerNameRepository.delete(writerName);
    }

    @Transactional
    public void setWriterNameMoimNull(final WriterName writerName) {
        writerName.setMoimNull();
    }

}
