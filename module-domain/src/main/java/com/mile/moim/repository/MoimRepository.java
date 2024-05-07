package com.mile.moim.repository;

import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MoimRepository extends JpaRepository<Moim, Long>, MoimRepositoryCustom {
    List<Post> getPostsById(final Long id);
    Boolean existsByName(final String name);

    @Query("SELECT m FROM Post p JOIN p.topic t JOIN t.moim m WHERE m.isPublic = true AND p.createdAt BETWEEN :startOfWeek AND :endOfWeek GROUP BY m ORDER BY COUNT(p) DESC")
    List<Moim> findTop3PublicMoimsWithMostPostsLastWeek(Pageable pageable, @Param("startOfWeek") LocalDateTime startOfWeek, @Param("endOfWeek") LocalDateTime endOfWeek);

    @Query("SELECT m FROM Post p JOIN p.topic t JOIN t.moim m WHERE m.isPublic = true AND m NOT IN :excludeMoims GROUP BY m ORDER BY MAX(p.createdAt) DESC")
    List<Moim> findLatestMoimsWithExclusion(Pageable pageable, @Param("excludeMoims") List<Moim> excludeMoims);

    @Query("SELECT m FROM Post p JOIN p.topic t JOIN t.moim m WHERE m.isPublic = true GROUP BY m ORDER BY MAX(p.createdAt) DESC")
    List<Moim> findLatestMoimsWithoutExclusion(Pageable pageable);
}
