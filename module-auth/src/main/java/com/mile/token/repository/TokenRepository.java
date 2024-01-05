package com.mile.token.repository;

import com.mile.token.domain.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token, Long> {

    Optional<Token> findByRefreshToken(final String refreshToken);
    Optional<Token> findById(final Long id);
}