package com.mile.user.service;

import com.mile.client.SocialType;
import com.mile.strategy.dto.UserInfoResponse;
import com.mile.user.domain.User;
import com.mile.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCreator {

    private final UserRepository userRepository;

    public Long createUser(final String socialId, final SocialType socialType, final String email) {
        User user = User.of(
                socialId,
                email,
                socialType
        );
        userRepository.save(user);
        return user.getId();
    }

}
