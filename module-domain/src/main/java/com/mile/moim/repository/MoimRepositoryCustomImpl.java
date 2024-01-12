package com.mile.moim.repository;

import static com.mile.moim.domain.QMoim.moim;
import static com.mile.post.domain.QPost.post;

import com.mile.moim.domain.Moim;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MoimRepositoryCustomImpl implements MoimRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public List<Moim> findTop3MoimsByPostCount() {

        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);

        List<Moim> result = queryFactory
                .select(moim)
                .from(moim)
                .leftJoin(post).on(moim.eq(post.topic.moim))
                .where(post.createdAt.after(oneWeekAgo))
                .groupBy(moim)
                .orderBy(post.id.count().desc())
                .limit(3)
                .fetch();

        return result;
    }
}
