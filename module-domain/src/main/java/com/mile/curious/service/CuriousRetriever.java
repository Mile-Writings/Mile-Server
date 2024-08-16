package com.mile.curious.service;

import com.mile.curious.domain.Curious;
import com.mile.curious.repository.CuriousRepository;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.ConflictException;
import com.mile.exception.model.NotFoundException;
import com.mile.post.domain.Post;
import com.mile.writername.domain.WriterName;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CuriousRetriever {

    private final CuriousRepository curiousRepository;

    public List<Curious> findAllByWriterName(final WriterName writerName) {
        return curiousRepository.findAllByWriterName(writerName);
    }

    public void checkCuriousExists(final Post post, final WriterName writerName) {
        if (curiousRepository.existsByPostAndWriterName(post, writerName)) {
            throw new ConflictException(ErrorMessage.CURIOUS_ALREADY_EXISTS_EXCEPTION);
        }
    }

    public void checkCuriousNotExists(final Post post, final WriterName writerName) {
        if (!curiousRepository.existsByPostAndWriterName(post, writerName)) {
            throw new NotFoundException(ErrorMessage.CURIOUS_NOT_FOUND);
        }
    }

    public boolean findCuriousExists(final Post post, final WriterName writerName) {
        return curiousRepository.existsByPostAndWriterName(post, writerName);
    }
}
