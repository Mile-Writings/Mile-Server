package com.mile.writername.repository;

import com.mile.moim.domain.Moim;
import com.mile.user.domain.User;
import com.mile.writername.domain.WriterName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface WriterNameRepository extends JpaRepository<WriterName, Long> {

    Optional<WriterName> findByMoimIdAndWriterId(final Long moimId, final Long userId);

    boolean existsWriterNameByMoimIdAndWriterId(final Long moimId, final Long userId);

    List<WriterName> findByWriter(final User user);

    List<WriterName> findByMoimId(final Long moimId);

    boolean existsWriterNameByMoimAndNormalizedName(final Moim moim, final String normalizedName);

    List<WriterName> findTop2ByMoimIdAndTotalCuriousCountGreaterThanOrderByTotalCuriousCountDesc(final Long moimId, final int totalCuriousCount);

    Page<WriterName> findByMoimIdOrderByIdDesc(Long moimId, Pageable pageable);

    Optional<WriterName> findById(final Long id);

    @Query("select w from WriterName w join fetch w.moim where w.writer.id = :writerId")
    List<WriterName> findAllByWriterId(final Long writerId);

    Integer countAllByWriter(final User user);

    @Transactional
    @Modifying
    @Query("DELETE FROM WriterName w WHERE w.moim = :moim AND w != :owner")
    void deleteWritersExceptOwner(@Param("moim") Moim moim, @Param("owner") WriterName owner);
}