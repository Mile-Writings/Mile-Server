package com.mile.moim.repository;

import com.mile.moim.domain.Moim;
import com.mile.moim.domain.QMoim;
import com.mile.post.domain.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MoimRepositoryCustomImpl implements MoimRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public List<Moim> findTop3MoimsByPostCount() {
        QMoim moim = QMoim.moim;
        QPost post = QPost.post;

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
