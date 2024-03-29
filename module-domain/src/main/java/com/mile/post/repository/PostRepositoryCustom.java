package com.mile.post.repository;

import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import com.mile.writername.domain.WriterName;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> findTop2ByMoimOrderByCuriousCountDesc(final Moim requestMoim);
    List<Post> findLatest4NonTemporaryPostsByMoim(Moim moim);
    List<Post> findByMoimAndWriterNameWhereIsTemporary(final Moim moim, final WriterName writerName);
}

