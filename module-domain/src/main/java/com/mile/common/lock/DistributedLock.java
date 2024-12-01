package com.mile.common.lock;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.MileException;
import com.mile.slack.module.SendErrorModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisException;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
@Slf4j
public class DistributedLock {

    private final RedissonClient redissonClient;
    private final SendErrorModule sendErrorModule;

    public void getLock(final String key) {
        try{
            final RLock lock = redissonClient.getLock(key);
            try {
                checkAvailability(lock.tryLock(2, 3, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } catch (RedisException e) {
            log.error("레디스 락 획득 중 에러 발생");
            sendErrorModule.sendError(e);
        }
    }

    public void afterLock(final String key) {

       try {
           final RLock lock = redissonClient.getLock(key);
           lock.unlock();
       } catch (RedisException e) {
           log.error("레디스 락 해제 중 에러 발생");
           sendErrorModule.sendError(e);
       }
    }

    public void checkAvailability(final Boolean available) {
        if (!available) throw new MileException(ErrorMessage.TIME_OUT_EXCEPTION);
    }

}
