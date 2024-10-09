package com.mile.curious.repository;

import com.mile.curious.repository.dto.PostAndCuriousCountInLastWeek;
import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.mile.curious.domain.QCurious.curious;
import static com.mile.post.domain.QPost.post;
import static com.mile.moim.domain.QMoim.moim;
import static com.mile.topic.domain.QTopic.topic;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class CuriousRepositoryCustomImpl implements CuriousRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostAndCuriousCountInLastWeek> findMostCuriousPostBeforeOneWeek(final Moim targetMoim, final LocalDateTime now) {
        final long WEEK = 7L;
        return queryFactory.select(Projections.constructor(PostAndCuriousCountInLastWeek.class, post, curious.count().as("count")))
                .from(curious)
                .join(curious.post, post)
                .join(topic).on(post.topic.id.eq(topic.id))
                .join(moim).on(topic.moim.id.eq(moim.id))
                .where(moim.id.eq(targetMoim.getId()))
                .where(curious.createdAt.between(now.minusDays(WEEK), now))
                .groupBy(post.id)
                .fetch();
    }

    @Override
    public List<Post> findPostByLatestCurious(final Moim targetMoim, final int requestSize, final List<Post> posts) {
        return queryFactory.select(post)
                .from(curious)
                .join(curious.post, post)
                .join(topic).on(post.topic.id.eq(topic.id))
                .join(moim).on(topic.moim.id.eq(moim.id))
                .where(moim.id.eq(targetMoim.getId()))
                .where(post.notIn(posts))
                .orderBy(curious.createdAt.desc())
                .limit(requestSize)
                .fetch();
    }
}
