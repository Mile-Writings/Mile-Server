package com.mile.user.repository;

import com.mile.external.client.SocialType;
import com.mile.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.mile.user.domain.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<User> findBySocialTypeAndSocialId(
            final Long socialId,
            final SocialType socialType
    ) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(user)
                        .where(
                                user.socialId.eq(socialId),
                                user.socialType.eq(socialType)
                        )
                        .fetchOne()
        );
    }
}
