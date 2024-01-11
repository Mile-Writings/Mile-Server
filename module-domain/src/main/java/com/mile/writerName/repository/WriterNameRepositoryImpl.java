package com.mile.writerName.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.mile.moim.domain.QMoim.moim;
import static com.mile.writerName.domain.QWriterName.writerName;

@RequiredArgsConstructor
public class WriterNameRepositoryImpl implements WriterNameRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public String getOwnerWriterNameByMoimId(final Long moimId) {
        return jpaQueryFactory.selectFrom(writerName)
                .select(writerName.name)
                .leftJoin(moim)
                .on(writerName.writer.eq(moim.owner))
                .fetchOne();
    }
}
