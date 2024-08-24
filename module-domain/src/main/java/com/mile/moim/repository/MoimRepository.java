package com.mile.moim.repository;

import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import com.mile.writername.domain.WriterName;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MoimRepository extends JpaRepository<Moim, Long> {
    Boolean existsByNormalizedName(final String normalizedName);

    @Query("SELECT m FROM Post p JOIN p.topic t JOIN t.moim m WHERE m.isPublic = true AND p.isTemporary = false AND p.createdAt BETWEEN :startOfWeek AND :endOfWeek GROUP BY m ORDER BY COUNT(p)")
    List<Moim> findTop3PublicMoimWithMostPostsLastWeek(final Pageable pageable, final @Param("startOfWeek") LocalDateTime startOfWeek, final @Param("endOfWeek") LocalDateTime endOfWeek);

    @Query("SELECT m FROM Post p JOIN p.topic t JOIN t.moim m WHERE m.isPublic = true AND p.isTemporary = false AND m NOT IN :excludeMoims GROUP BY m ORDER BY MAX(p.createdAt) DESC")
    List<Moim> findLatestMoimWithExclusion(final Pageable pageable, final @Param("excludeMoims") List<Moim> excludeMoims);

    @Query("SELECT m FROM Post p JOIN p.topic t JOIN t.moim m WHERE m.isPublic = true GROUP BY m ORDER BY MAX(p.createdAt) DESC")
    List<Moim> findLatestMoimsWithoutExclusion(Pageable pageable);

    Optional<Moim> findByOwner(final WriterName writerName);
}
