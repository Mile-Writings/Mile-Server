package com.mile.topic.repository;

import com.mile.moim.domain.Moim;

import java.util.Optional;

public interface TopicRepositoryCustom {

    Optional<String> findLatestTopicByMoim(final Moim moim);

}
