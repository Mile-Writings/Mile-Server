package com.mile.moim.service.lock;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.MileException;
import com.mile.moim.domain.Moim;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@RequiredArgsConstructor
@Component
public class MoimPopularInfoAspect {

    private final RedissonClient redissonClient;
    private final static String MOIM_POPULAR_LOCK = "MOIM_POPULAR_LOCK : ";
    private final AopForTransaction aopForTransaction;

    @Pointcut("@annotation(com.mile.moim.service.lock.AtomicMoimPopulerInfo)")
    public void setMoimPoplarInfoCut() {
    }

    @Around("setMoimPoplarInfoCut()")
    public Object getLockForPopularInfoTransaction(final ProceedingJoinPoint joinPoint) throws Throwable {
        final Moim moim = (Moim) joinPoint.getArgs()[0];

        final RLock lock = redissonClient.getLock(MOIM_POPULAR_LOCK + moim.getId().toString());

        try {
            checkAvailability(lock.tryLock(3, 4, TimeUnit.SECONDS));
            return aopForTransaction.proceed(joinPoint);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public void checkAvailability(final Boolean available) {
        if (!available) throw new MileException(ErrorMessage.TIME_OUT_EXCEPTION);
    }
}
