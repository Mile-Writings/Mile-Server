package com.mile.writerName.repository;

import com.mile.writerName.domain.WriterName;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WriterNameRepository extends JpaRepository<WriterName, Long>, WriterNameRepositoryCustom {

    Optional<WriterName> findByMoimIdAndWriterId(final Long moimId, final Long userId);
    List<WriterName> findByMoimId(final Long moimId);
    WriterName findByWriterId(final Long userId);
    List<WriterName> findTop2ByMoimIdOrderByTotalCuriousCountDesc(final Long moimid);

}