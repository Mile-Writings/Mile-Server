package com.mile.user.service;


import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;
import com.mile.user.domain.User;
import com.mile.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRemover {

    private final UserRetriever userRetriever;
    private final UserRepository userRepository;

    public void delete(
            final Long userId
    ) {
        User user = userRetriever.findById(userId);
        userRepository.delete(user);
    }

}