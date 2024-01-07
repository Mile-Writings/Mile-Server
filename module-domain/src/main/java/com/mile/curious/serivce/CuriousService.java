package com.mile.curious.serivce;

import com.mile.curious.domain.Curious;
import com.mile.curious.repository.CuriousRepository;
import com.mile.post.domain.Post;
import com.mile.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CuriousService {

    private final CuriousRepository curiousRepository;

    public void createCurious(final Post post, final User user) {
        curiousRepository.save(Curious.create(post, user));
    }
}
