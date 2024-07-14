package com.mile.user.service;


import com.mile.user.domain.User;
import com.mile.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRemover {

    private final UserRepository userRepository;

    public void delete(
            final User user
    ) {
        userRepository.delete(user);
    }

}