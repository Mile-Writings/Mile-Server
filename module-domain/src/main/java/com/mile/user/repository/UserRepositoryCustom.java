package com.mile.user.repository;

import com.mile.client.SocialType;
import com.mile.user.domain.User;

import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<User> findBySocialTypeAndSocialId(final String socialId, final SocialType socialType);
}
