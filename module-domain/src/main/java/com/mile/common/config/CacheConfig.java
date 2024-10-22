package com.mile.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager
                .setAllowNullValues(false);
        cacheManager
                .setCaffeine(caffeineConfig());
        return cacheManager;
    }

    private Scheduler getScheduler() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
        Scheduler scheduler;
        scheduler = Scheduler.forScheduledExecutorService(executor);
        return scheduler;
    }

    private Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .maximumSize(200)
                .expireAfterAccess(1, TimeUnit.DAYS)
                .scheduler(getScheduler());
    }
}
