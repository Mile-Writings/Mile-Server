package com.mile.common.lock;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@RequiredArgsConstructor
@Component
public class MoimNameRequestAspect {

    private final DistributedLock distributedLock;
    private final static String MOIM_NAME_LOCK = "MOIM_NAME_LOCK";
    private final AopForTransaction aopForTransaction;

    @Pointcut("@annotation(com.mile.common.lock.AtomicValidateUniqueMoimName)")
    public void uniqueMoimNameCut() {
    }

    @Around("uniqueMoimNameCut()")
    public Object validateUniqueName(final ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            distributedLock.getLock(MOIM_NAME_LOCK);
            return aopForTransaction.proceed(joinPoint);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            distributedLock.afterLock(MOIM_NAME_LOCK);
        }
    }

}
