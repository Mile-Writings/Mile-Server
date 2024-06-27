package com.mile.user.service;

import com.mile.client.SocialType;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;
import com.mile.user.domain.User;
import com.mile.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRetriever {
    private final UserRepository userRepository;

    public boolean isExistingUser(
            final String socialId,
            final SocialType socialType
    ) {
        return userRepository.findBySocialTypeAndSocialId(socialId, socialType).isPresent();
    }


    public User getBySocialId(
            final String socialId,
            final SocialType socialType
    ) {
        return userRepository.findBySocialTypeAndSocialId(socialId, socialType).orElseThrow(
                () -> new NotFoundException(ErrorMessage.USER_NOT_FOUND)
        );
    }

    public User findById(
            Long userId
    ) {
        return userRepository.findById(userId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.USER_NOT_FOUND)
                );
    }
}
