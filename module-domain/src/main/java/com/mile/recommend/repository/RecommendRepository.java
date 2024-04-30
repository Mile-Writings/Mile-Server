package com.mile.recommend.repository;

import com.mile.recommend.domain.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {
}
