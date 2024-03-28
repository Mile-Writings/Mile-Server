package com.mile.topic.repository;

import com.mile.topic.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long>, TopicRepositoryCustom {

    List<Topic> findByMoimId(final Long moimId);
    Long countByMoimId(final Long moimId);

}
