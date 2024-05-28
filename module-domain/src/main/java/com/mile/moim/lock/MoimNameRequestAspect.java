package com.mile.moim.lock;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

@Aspect
@RequiredArgsConstructor
public class MoimNameRequestAspect {

    private final RedissonClient redissonClient;
    private final static String MOIM_NAME_LOCK = "MOIM NAME";
    private final AopForTransaction aopForTransaction;

    @Pointcut("execution(* com.mile.moim.service.MoimService.*(..))")
    public void uniqueMoimNameCut() {
    }

    @Around("uniqueMoimNameCut()")
    public Object validateUniqueName(final ProceedingJoinPoint joinPoint) throws Throwable {
        final RLock lock = redissonClient.getLock(MOIM_NAME_LOCK);
        try {
            checkAvailability(lock.tryLock(3, 4, TimeUnit.SECONDS));
            return aopForTransaction.proceed(joinPoint);
        } finally {
            lock.unlock();
        }
    }


    public void checkAvailability(final Boolean available) {
        if (!available) throw new RuntimeException("Lock is Unavailable");
    }

}
