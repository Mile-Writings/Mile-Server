package com.mile.moim.repository;

import com.mile.moim.domain.Moim;
import java.util.List;

public interface MoimRepositoryCustom {

    List<Moim> findTop3MoimsByPostCountInLastWeek();
}
