package com.mile.writername.repository;

import com.mile.moim.domain.Moim;
import com.mile.user.domain.User;
import com.mile.writername.domain.WriterName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WriterNameRepository extends JpaRepository<WriterName, Long> {

    Optional<WriterName> findByMoimIdAndWriterId(final Long moimId, final Long userId);

    boolean existsWriterNameByMoimIdAndWriterId(final Long moimId, final Long userId);

    List<WriterName> findByMoimId(final Long moimId);


    boolean existsWriterNameByMoimAndName(final Moim moim, final String name);

    Optional<WriterName> findByWriterId(final Long userId);

    List<WriterName> findTop2ByMoimIdAndTotalCuriousCountGreaterThanOrderByTotalCuriousCountDesc(final Long moimId, final int totalCuriousCount);

    Page<WriterName> findByMoimIdOrderByIdDesc(Long moimId, Pageable pageable);

    Optional<WriterName> findById(final Long id);

    List<WriterName> findAllByWriterId(final Long writerId);

    Integer countAllByWriter(final User user);
}