package com.mile.curious.repository;

import com.mile.curious.domain.Curious;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuriousRepository extends JpaRepository<Curious, Long> {
}
