package com.mile.user.repository;

import com.mile.external.client.SocialType;
import com.mile.user.domain.User;

import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<User> findBySocialTypeAndSocialId(final Long socialId, final SocialType socialType);
}
