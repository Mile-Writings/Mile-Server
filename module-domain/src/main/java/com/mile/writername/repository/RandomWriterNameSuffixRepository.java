package com.mile.writername.repository;

import com.mile.writername.domain.RandomWriterNameSuffix;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RandomWriterNameSuffixRepository extends JpaRepository<RandomWriterNameSuffix, Long> {
}
