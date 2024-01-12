package com.mile.moim.repository;

import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MoimRepository extends JpaRepository<Moim, Long>, MoimRepositoryCustom {
    List<Post> getPostsById(final Long id);
}
