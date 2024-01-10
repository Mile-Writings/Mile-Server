package com.mile.writerName.repository;

import com.mile.writerName.domain.WriterName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WriterNameRepository extends JpaRepository<WriterName, Long>, WriterNameRepositoryCustom {

    Optional<WriterName> findByMoimIdAndWriterId(final Long moimId, final Long userId);

    List<WriterName> findByMoimId(final Long moimId);
}