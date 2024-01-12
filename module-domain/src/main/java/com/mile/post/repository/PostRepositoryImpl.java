package com.mile.post.repository;

import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import com.mile.writerName.domain.WriterName;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.mile.moim.domain.QMoim.moim;
import static com.mile.post.domain.QPost.post;
import static com.mile.writerName.domain.QWriterName.writerName;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    // select post from post join moim
    public List<Post> findTop2ByMoimOrderByCuriousCountDesc(final Moim requestMoim) {
        return jpaQueryFactory.selectFrom(post)
                .join(moim)
                .on(post.topic.moim.eq(requestMoim))
                .orderBy(post.curiousCount.desc())
                .stream().limit(2).collect(Collectors.toList());
    }

    public List<Post> findByMoimAndWriterNameWhereIsTemporary(final Moim requestMoim, final WriterName requestWriterName) {
        return jpaQueryFactory.selectFrom(post)
                .join(moim)
                .on(post.topic.moim.eq(requestMoim))
                .join(writerName)
                .on(post.writerName.eq(requestWriterName))
                .where(post.isTemporary.eq(true))
                .stream().toList();
    }
}
