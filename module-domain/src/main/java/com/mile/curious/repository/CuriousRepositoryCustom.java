package com.mile.curious.repository;

import com.mile.curious.repository.dto.PostAndCuriousCountInLastWeek;
import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

public interface CuriousRepositoryCustom {
    List<PostAndCuriousCountInLastWeek> findMostCuriousPostBeforeOneWeek(final Moim moim, final LocalDateTime now);
    List<Post> findPostByLatestCurious(final Moim moim, final int requestSize, final List<Post> posts);
}
