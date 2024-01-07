package com.mile.moim.repository;

import com.mile.moim.domain.Moim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoimRepository extends JpaRepository<Moim, Long> {
}
