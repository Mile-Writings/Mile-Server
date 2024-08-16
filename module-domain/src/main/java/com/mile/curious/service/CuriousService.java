package com.mile.curious.service;

import com.mile.curious.service.dto.CuriousInfoResponse;
import com.mile.post.domain.Post;
import com.mile.writername.domain.WriterName;
import com.mile.writername.service.WriterNameUpdator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CuriousService {

    private final WriterNameUpdator writerNameUpdator;
    private final CuriousRemover curiousRemover;
    private final CuriousRetriever curiousRetriever;
    private final CuriousCreator curiousCreator;

    public void deleteCurious(final Post post, final WriterName writerName) {
        curiousRetriever.checkCuriousNotExists(post, writerName);
        curiousRemover.deleteCurious(post, writerName);
        post.decreaseCuriousCount();
        writerNameUpdator.decreaseTotalCuriousCountByWriterName(writerName);
    }

    public void createCurious(final Post post, final WriterName writerName) {
        curiousRetriever.checkCuriousExists(post, writerName);
        curiousCreator.createCurious(post, writerName);
        post.increaseCuriousCount();
        writerNameUpdator.increaseTotalCuriousCountByWriterName(writerName);
    }


    public CuriousInfoResponse getCuriousInfoOfPostAndWriterName(final Post post, final WriterName writerName) {
        return CuriousInfoResponse.of(curiousRetriever.findCuriousExists(post, writerName), post.getCuriousCount());
    }


}