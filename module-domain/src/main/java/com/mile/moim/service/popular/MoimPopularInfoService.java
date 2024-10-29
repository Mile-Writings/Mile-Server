package com.mile.moim.service.popular;

import com.mile.moim.domain.Moim;
import com.mile.moim.domain.popular.MoimPopularInfo;
import com.mile.moim.repository.MoimPopularInfoRepository;
import com.mile.moim.service.lock.AtomicValidateMoimPopulerInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MoimPopularInfoService {
    private final MoimPopularInfoRepository moimPopularInfoRepository;
    private final MoimPopularInfoRegister moimPopularInfoRegister;


    @Cacheable(value = "moimPopularInfo", key = "#moim.id")
    @AtomicValidateMoimPopulerInfo
    public MoimPopularInfo getMoimPopularInfo(final Moim moim) {
        return moimPopularInfoRepository.findByMoimId(moim.getId()).orElseGet(
                () -> moimPopularInfoRegister.setMostPopularInfoOfMoim(moim)
        );
    }

}
