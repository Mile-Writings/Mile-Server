package com.mile.post.repository;

import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import com.mile.writerName.domain.WriterName;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.mile.moim.domain.QMoim.moim;
import static com.mile.post.domain.QPost.post;
import static com.mile.topic.domain.QTopic.topic;
import static com.mile.writerName.domain.QWriterName.writerName;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    // select p from post p join moim m on p.topic = m.topic orderby p.curiousCount limit 2
    public List<Post> findTop2ByMoimOrderByCuriousCountDesc(final Moim requestMoim) {
        return jpaQueryFactory
                .selectFrom(post)
                .join(topic).on(post.topic.eq(topic))
                .join(moim).on(topic.moim.eq(moim))
                .where(moim.eq(requestMoim))
                .orderBy(post.curiousCount.desc())
                .limit(2)
                .fetch();

    }

    public List<Post> findLatest4NonTemporaryPostsByMoim(Moim moim) {

        List<Post> result = jpaQueryFactory
                .select(post)
                .from(post)
                .where(post.topic.moim.eq(moim).and(post.isTemporary.eq(false)))
                .orderBy(post.createdAt.desc())
                .limit(4)
                .fetch();

        return result;
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
