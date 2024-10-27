package com.mile.post.repository;

import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import com.mile.topic.domain.Topic;
import com.mile.writername.domain.WriterName;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Optional;

import static com.mile.moim.domain.QMoim.moim;
import static com.mile.post.domain.QPost.post;
import static com.mile.topic.domain.QTopic.topic;
import static com.mile.writername.domain.QWriterName.writerName;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

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

    public Optional<Post> findByMoimAndWriterNameWhereIsTemporary(final Moim requestMoim, final Long requestWriterNameId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(post)
                .join(moim)
                .on(post.topic.moim.eq(requestMoim))
                .join(writerName)
                .on(post.writerName.id.eq(requestWriterNameId))
                .where(post.isTemporary.eq(true)).fetchOne());
    }

    public Slice<Post> findByTopicAndLastPostId(final Topic topic, final Pageable pageable, final Long lastPostId) {
        List<Post> result = jpaQueryFactory.selectFrom(post)
                .where(post.topic.eq(topic))
                .orderBy(post.id.desc())
                .where(lessThanLastPostId(lastPostId))
                .where(post.isTemporary.eq(false))
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, result);
    }

    private BooleanExpression lessThanLastPostId(final Long lastPostId) {
        return lastPostId != null ? post.id.lt(lastPostId) : null;
    }

    private Slice<Post> checkLastPage(final Pageable pageable, final List<Post> posts) {
        boolean hasNext = false;
        if (posts.size() > pageable.getPageSize()) {
            posts.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(posts, pageable, hasNext);
    }

}
