package com.mile.topic.repository;

import com.mile.moim.domain.Moim;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.mile.topic.domain.QTopic.topic;

@RequiredArgsConstructor
public class TopicRepositoryImpl implements TopicRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<String> findLatestTopicByMoim(final Moim moim) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(topic)
                .select(topic.content)
                .where(topic.moim.eq(moim))
                .orderBy(topic.createdAt.desc())
                .fetchFirst());
    }
}
