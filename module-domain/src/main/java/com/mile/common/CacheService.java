package com.mile.common;

import com.mile.slack.module.SendMessageModule;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final String MOIM_CACHE_NAME = "moimPopularInfo";
    private final CacheManager cacheManager;
    private final SendMessageModule sendMessageModule;

    public void deleteMoimCache() {
        sendMessageModule.sendMessage("INTERNAL API 호출) 글모임 별 인기 글/ 작가에 대한 캐시 삭제 완료");
        if (cacheManager.getCache(MOIM_CACHE_NAME) != null) {
            cacheManager.getCache(MOIM_CACHE_NAME).clear();
        }
    }
}
