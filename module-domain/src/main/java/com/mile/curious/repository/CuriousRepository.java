package com.mile.curious.repository;

import com.mile.curious.domain.Curious;
import com.mile.post.domain.Post;
import com.mile.writername.domain.WriterName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CuriousRepository extends JpaRepository<Curious, Long>, CuriousRepositoryCustom {
    boolean existsByPostAndWriterName(final Post post, final WriterName writerName);

    Curious findByPostAndWriterName(final Post post, final WriterName writerName);

    @Transactional
    @Modifying
    @Query("delete from Curious c where c.post = :post")
    void deleteAllByPost(@Param("post") final Post post);

    List<Curious> findAllByWriterName(final WriterName writerName);
}
