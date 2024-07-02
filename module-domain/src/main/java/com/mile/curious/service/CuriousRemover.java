package com.mile.curious.service;


import com.mile.curious.domain.Curious;
import com.mile.curious.repository.CuriousRepository;
import com.mile.post.domain.Post;
import com.mile.writername.domain.WriterName;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CuriousRemover {
    private final CuriousRepository curiousRepository;
    private final CuriousRetriever curiousRetriever;

    public void deleteAllByWriterNameId(
            final WriterName writerName
    ) {

        List<Curious> curiousList = curiousRetriever.findAllByWriterName(writerName);

        curiousList.forEach(curious -> {
            Post post = curious.getPost();
            post.decreaseCuriousCount();
            writerName.decreaseTotalCuriousCount();
        });

        curiousRepository.deleteAll(curiousList);
    }
    public void deleteAllByPost(
            final Post post
    ) {
        curiousRepository.deleteAllByPost(post);
    }
}
