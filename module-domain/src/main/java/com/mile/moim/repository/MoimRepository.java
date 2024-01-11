package com.mile.moim.repository;

import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoimRepository extends JpaRepository<Moim, Long> {
    List<Post> getPostsById(final Long id);
}
