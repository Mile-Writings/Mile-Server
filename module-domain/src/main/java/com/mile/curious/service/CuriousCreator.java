package com.mile.curious.service;

import com.mile.curious.domain.Curious;
import com.mile.curious.repository.CuriousRepository;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.ConflictException;
import com.mile.post.domain.Post;
import com.mile.writername.domain.WriterName;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CuriousCreator {
    private final CuriousRepository curiousRepository;

    public void createCurious(final Post post, final WriterName writerName) {
        try {
            curiousRepository.save(Curious.create(post, writerName));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(ErrorMessage.CURIOUS_ALREADY_EXISTS_EXCEPTION);
        }
    }

}
