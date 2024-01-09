package com.mile.curious.serivce;

import com.mile.curious.domain.Curious;
import com.mile.curious.repository.CuriousRepository;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.ConflictException;
import com.mile.exception.model.NotFoundException;
import com.mile.post.domain.Post;
import com.mile.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CuriousService {

    private final CuriousRepository curiousRepository;

    public void deleteCurious(final Post post, final User user) {
        checkCuriousNotExists(post, user);
        curiousRepository.delete(curiousRepository.findByPostAndUser(post, user));
    }

    public void checkCuriousNotExists(final Post post, final User user) {
        if (!curiousRepository.existsByPostAndUser(post, user)) {
            throw new NotFoundException(ErrorMessage.CURIOUS_NOT_FOUND);
        }
    }
}
