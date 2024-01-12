package com.mile.post.repository;

import static com.mile.moim.domain.QMoim.moim;
import static com.mile.post.domain.QPost.post;

import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import com.mile.post.domain.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

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

    public List<Post> findLatest4PostsByMoim(Moim moim) {
        QPost post = QPost.post;

        List<Post> result = jpaQueryFactory
                .select(post)
                .from(post)
                .where(post.topic.moim.eq(moim))
                .orderBy(post.createdAt.desc())
                .limit(4)
                .fetch();

        return result;
    }

}
