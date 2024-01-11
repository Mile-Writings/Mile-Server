package com.mile.post.repository;

import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> findTop2ByMoimOrderByCuriousCountDesc(final Moim requestMoim);
}
