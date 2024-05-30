package com.mile.moim.service.lock;

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
public class MoimNameRequestAspect {

    private final RedissonClient redissonClient;
    private final static String MOIM_NAME_LOCK = "MOIM_NAME_LOCK : ";
    private final AopForTransaction aopForTransaction;

    @Pointcut("@annotation(com.mile.moim.service.lock.AtomicValidateUniqueMoimName)")
    public void uniqueMoimNameCut() {
    }

    @Around("uniqueMoimNameCut()")
    public Object validateUniqueName(final ProceedingJoinPoint joinPoint) throws Throwable {
        final String key = MOIM_NAME_LOCK + joinPoint.getSignature().getName();
        final RLock lock = redissonClient.getLock(key);
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
        if (!available) throw new RuntimeException("Lock is Unavailable");
    }

}
