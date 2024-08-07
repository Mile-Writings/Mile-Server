package com.mile.topic.repository;

import com.mile.moim.domain.Moim;
import com.mile.topic.domain.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long>, TopicRepositoryCustom {

    List<Topic> findByMoim(final Moim moim);

    List<Topic> findByMoimId(@NonNull final Long moimId);

    Long countByMoimId(final Long moimId);

    Page<Topic> findByMoimIdOrderByCreatedAtDesc(Long moimId, Pageable pageable);

    @Transactional
    @Modifying
    @Query("DELETE FROM Topic t where t.moim = :moim")
    void deleteByMoim(@Param("moim") Moim moim);
}
