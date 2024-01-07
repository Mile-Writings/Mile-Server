package com.mile.curious.serivce;

import com.mile.curious.domain.Curious;
import com.mile.curious.repository.CuriousRepository;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.exception.model.ForbiddenException;
import com.mile.exception.model.MileException;
import com.mile.post.domain.Post;
import com.mile.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CuriousService {

    private final CuriousRepository curiousRepository;

    public void createCurious(final Post post, final User user) {
        checkCuriousExists(post, user);
        curiousRepository.save(Curious.create(post, user));
    }

    public void checkCuriousExists(final Post post, final User user) {
        if (curiousRepository.existsByPostAndUser(post, user)) {
            throw new BadRequestException(ErrorMessage.CURIOUS_ALREADY_EXISTS_EXCEPTION);
        }
    }
}
