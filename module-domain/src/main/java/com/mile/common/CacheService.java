package com.mile.common;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final String MOIM_CACHE_NAME = "moimPopularInfo";
    private final CacheManager cacheManager;

    public void deleteMoimCache() {
        if (cacheManager.getCache(MOIM_CACHE_NAME) != null) {
            cacheManager.getCache(MOIM_CACHE_NAME).clear();
        }
    }
}
