package com.mile.post.repository;

import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import com.mile.topic.domain.Topic;
import com.mile.writername.domain.WriterName;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface PostRepositoryCustom {
    List<Post> findTop2ByMoimOrderByCuriousCountDesc(final Moim requestMoim);

    List<Post> findLatest4NonTemporaryPostsByMoim(final Moim moim);

    Optional<Post> findByMoimAndWriterNameWhereIsTemporary(final Moim moim, final WriterName writerName);

    Slice<Post> findByTopicAndLastPostId(final Topic topic, final Pageable pageable, final Long lastPostId);
}