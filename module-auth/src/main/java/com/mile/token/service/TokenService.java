package com.mile.token.service;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;
import com.mile.token.domain.Token;
import com.mile.token.repository.TokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    @Transactional
    public void saveRefreshToken(
            final Long userId,
            final String refreshToken
    ) {
        tokenRepository.save(
                Token.of(
                        userId,
                        refreshToken
                )
        );
    }

    public Long findIdByRefreshToken(
            final String refreshToken
    ) {
        Token token = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.REFRESH_TOKEN_NOT_FOUND)
                );
        return token.getId();
    }

    @Transactional
    public void deleteRefreshToken(
            final Long userId
    ) {
        Token token = tokenRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.REFRESH_TOKEN_NOT_FOUND)
        );
        tokenRepository.delete(token);
    }
}
