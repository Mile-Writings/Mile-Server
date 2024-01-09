package com.mile.curious.serivce;
import com.mile.curious.repository.CuriousRepository;
import com.mile.curious.serivce.dto.CuriousInfoResponse;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;
import com.mile.curious.domain.Curious;
import com.mile.exception.model.ConflictException;
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
        post.decreaseCuriousCount();
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
    }

    public void checkCuriousExists(final Post post, final User user) {
        if (curiousRepository.existsByPostAndUser(post, user)) {
            throw new ConflictException(ErrorMessage.CURIOUS_ALREADY_EXISTS_EXCEPTION);
        }
    }

    public CuriousInfoResponse getCuriousInfoResponse(final Post post, final User user) {
        return CuriousInfoResponse.of(curiousRepository.existsByPostAndUser(post, user), post.getCuriousCount());
    }
}