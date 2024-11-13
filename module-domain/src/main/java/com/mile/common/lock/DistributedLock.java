package com.mile.common.lock;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.MileException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class DistributedLock {

    private final RedissonClient redissonClient;

    public void getLock(final String key) {
        final RLock lock = redissonClient.getLock(key);
        try {
            checkAvailability(lock.tryLock(3, 4, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void afterLock(final String key) {
        final RLock lock = redissonClient.getLock(key);
        lock.unlock();
    }

    public void checkAvailability(final Boolean available) {
        if (!available) throw new MileException(ErrorMessage.TIME_OUT_EXCEPTION);
    }

}
