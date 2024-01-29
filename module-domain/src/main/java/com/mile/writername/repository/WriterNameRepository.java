package com.mile.writername.repository;

import com.mile.writername.domain.WriterName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WriterNameRepository extends JpaRepository<WriterName, Long> {

    Optional<WriterName> findByMoimIdAndWriterId(final Long moimId, final Long userId);

    List<WriterName> findByMoimId(final Long moimId);

    Optional<WriterName> findByWriterId(final Long userId);

    List<WriterName> findTop2ByMoimIdAndTotalCuriousCountGreaterThanOrderByTotalCuriousCountDesc(final Long moimId, final int totalCuriousCount);

}