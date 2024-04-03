package com.mile.topic.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.mile.topic.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long>, TopicRepositoryCustom {

    List<Topic> findByMoimId(final Long moimId);
    Page<Topic> findByMoimIdOrderByCreatedAtDesc(Long moimId, Pageable pageable);
    Long countByMoimId(final Long moimId);
}
