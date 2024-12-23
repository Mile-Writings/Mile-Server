package com.mile.moim.service.popular;

import com.mile.common.CacheService;
import com.mile.common.lock.DistributedLock;
import com.mile.moim.domain.Moim;
import com.mile.moim.domain.popular.MoimPopularInfo;
import com.mile.moim.repository.MoimPopularInfoRepository;
import com.mile.slack.module.SendMessageModule;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MoimPopularInfoService {
    private final MoimPopularInfoRepository moimPopularInfoRepository;
    private final MoimPopularInfoRegister moimPopularInfoRegister;
    private final SendMessageModule sendMessageModule;
    private final DistributedLock distributedLock;
    private final CacheService cacheService;


    @Cacheable(value = "moimPopularInfo", key = "#moim.id")
    public MoimPopularInfo getMoimPopularInfo(final Moim moim) {
        return moimPopularInfoRepository.findByMoimId(moim.getId()).orElseGet(
                () -> {
                    distributedLock.getLock("MOIM_POPULAR_LOCK" + moim.getId());
                    return moimPopularInfoRegister.setMostPopularInfoOfMoim(moim);
                }
        );
    }

    @Scheduled(cron = "59 59 23 * * SUN")
    public void deleteAllForScheduled() {
        sendMessageModule.sendMessage("글모임 별 인기 글/ 인기 작가 삭제 완료 : 총 " + moimPopularInfoRepository.countAll() + "개의 모임");
        moimPopularInfoRepository.deleteAllInBatch();
    }

    @Scheduled(cron = "59 59 23 * * *")
    public void deleteCacheForScheduled() {
        sendMessageModule.sendMessage("글모임 별 인기 글/인기 작가 캐시 삭제 완료");
        cacheService.deleteMoimCache();
    }

}
