package com.mile.moim.repository;

import com.mile.moim.domain.popular.MoimPopularInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Optional;

public interface MoimPopularInfoRepository extends JpaRepository<MoimPopularInfo, Long> {

    Optional<MoimPopularInfo> findByMoimId(final long moimId);


    @Scheduled(cron = "59 59 23 * * SUN")
    void deleteAll();
}
