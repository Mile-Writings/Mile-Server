package com.mile.curious.service;

import com.mile.curious.domain.Curious;
import com.mile.curious.repository.CuriousRepository;
import com.mile.curious.service.dto.CuriousInfoResponse;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.ConflictException;
import com.mile.exception.model.NotFoundException;
import com.mile.post.domain.Post;
import com.mile.user.domain.User;
import com.mile.writername.service.WriterNameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CuriousService {

    private final CuriousRepository curiousRepository;
    private final WriterNameService writerNameService;

    public void deleteCuriousOnPost(final Post post, final User user) {
        checkCuriousNotExists(post, user);
        curiousRepository.delete(curiousRepository.findByPostAndUser(post, user));
        post.decreaseCuriousCount();
        writerNameService.decreaseTotalCuriousCountByWriterId(user.getId());
    }

    public void checkCuriousNotExists(final Post post, final User user) {
        if (!curiousRepository.existsByPostAndUser(post, user)) {
            throw new NotFoundException(ErrorMessage.CURIOUS_NOT_FOUND);
        }
    }

    public void createCurious(final Post post, final User user) {
        checkCuriousExists(post, user);
        curiousRepository.save(Curious.create(post, user));
        post.increaseCuriousCount();
        writerNameService.increaseTotalCuriousCountByWriterId(user.getId());
    }

    public void checkCuriousExists(final Post post, final User user) {
        if (curiousRepository.existsByPostAndUser(post, user)) {
            throw new ConflictException(ErrorMessage.CURIOUS_ALREADY_EXISTS_EXCEPTION);
        }
    }

    public CuriousInfoResponse getCuriousInfoOfPostAndUser(final Post post, final User user) {
        return CuriousInfoResponse.of(curiousRepository.existsByPostAndUser(post, user), post.getCuriousCount());
    }

    public void deleteAllByPost(
            final Post post
    ) {
        curiousRepository.deleteAllByPost(post);
    }
}