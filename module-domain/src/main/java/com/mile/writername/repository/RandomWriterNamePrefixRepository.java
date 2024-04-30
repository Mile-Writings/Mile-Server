package com.mile.writername.repository;

import com.mile.writername.domain.RandomWriterNamePrefix;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RandomWriterNamePrefixRepository extends JpaRepository<RandomWriterNamePrefix, Long> {
}
