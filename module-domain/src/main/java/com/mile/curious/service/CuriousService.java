package com.mile.curious.service;

import com.mile.curious.domain.Curious;
import com.mile.curious.repository.CuriousRepository;
import com.mile.curious.service.dto.CuriousInfoResponse;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.ConflictException;
import com.mile.exception.model.NotFoundException;
import com.mile.post.domain.Post;
import com.mile.writername.domain.WriterName;
import com.mile.writername.service.WriterNameService;
import com.mile.writername.service.WriterNameUpdator;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CuriousService {

    private final CuriousRepository curiousRepository;
    private final WriterNameUpdator writerNameUpdator;

    public void deleteCurious(final Post post, final WriterName writerName) {
        checkCuriousNotExists(post, writerName);
        curiousRepository.delete(curiousRepository.findByPostAndWriterName(post, writerName));
        post.decreaseCuriousCount();
        writerNameUpdator.decreaseTotalCuriousCountByWriterName(writerName);
    }

    public void checkCuriousNotExists(final Post post, final WriterName writerName) {
        if (!curiousRepository.existsByPostAndWriterName(post, writerName)) {
            throw new NotFoundException(ErrorMessage.CURIOUS_NOT_FOUND);
        }
    }

    public void createCurious(final Post post, final WriterName writerName) {
        checkCuriousExists(post, writerName);
        try {
            curiousRepository.save(Curious.create(post, writerName));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(ErrorMessage.CURIOUS_ALREADY_EXISTS_EXCEPTION);
        }
        post.increaseCuriousCount();
        writerNameUpdator.increaseTotalCuriousCountByWriterName(writerName);
    }

    public void checkCuriousExists(final Post post, final WriterName writerName) {
        if (curiousRepository.existsByPostAndWriterName(post, writerName)) {
            throw new ConflictException(ErrorMessage.CURIOUS_ALREADY_EXISTS_EXCEPTION);
        }
    }

    public CuriousInfoResponse getCuriousInfoOfPostAndWriterName(final Post post, final WriterName writerName) {
        return CuriousInfoResponse.of(curiousRepository.existsByPostAndWriterName(post, writerName), post.getCuriousCount());
    }

    public void deleteAllByPost(
            final Post post
    ) {
        curiousRepository.deleteAllByPost(post);
    }

    public void deleteAllByPosts(
            final List<Post> posts
    ) {
        posts.forEach(this::deleteAllByPost);
    }

}