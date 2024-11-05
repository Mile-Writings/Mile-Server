package com.mile.moim.repository;

import com.mile.moim.domain.popular.MoimPopularInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Optional;

public interface MoimPopularInfoRepository extends JpaRepository<MoimPopularInfo, Long> {

    @Query("select m from MoimPopularInfo m join fetch m.posts join fetch m.writers where m.moimId = :moimId")
    Optional<MoimPopularInfo> findByMoimId(final long moimId);

    @Query("select count(m) from MoimPopularInfo m")
    Long countAll();
}
