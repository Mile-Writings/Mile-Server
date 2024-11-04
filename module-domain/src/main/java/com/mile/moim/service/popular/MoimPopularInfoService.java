package com.mile.moim.service.popular;

import com.mile.moim.domain.Moim;
import com.mile.moim.domain.popular.MoimPopularInfo;
import com.mile.moim.repository.MoimPopularInfoRepository;
import com.mile.slack.module.SendMessageModule;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MoimPopularInfoService {
    private final MoimPopularInfoRepository moimPopularInfoRepository;
    private final MoimPopularInfoRegister moimPopularInfoRegister;
    private final SendMessageModule sendMessageModule;


    @Cacheable(value = "moimPopularInfo", key = "#moim.id")
    public MoimPopularInfo getMoimPopularInfo(final Moim moim) {
        return moimPopularInfoRepository.findByMoimId(moim.getId()).orElseGet(
                () -> moimPopularInfoRegister.setMostPopularInfoOfMoim(moim)
        );
    }

    @Async
    @Scheduled(cron = "59 59 23 * * SUN")
    public void deleteAllForScheduled() {
        sendMessageModule.sendMessage("글모임 별 인기 글/ 인기 작가 삭제 완료 : 총 " + moimPopularInfoRepository.countAll() + "개의 모임");
        moimPopularInfoRepository.deleteAllInBatch();
    }

}
