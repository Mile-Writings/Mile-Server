package com.mile.common.log;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * from @sohyundoh
 * <p>
 * SQL 진입점을 로깅하기 위한 AOP 클래스
 */
@Aspect
@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class SqlFunctionLoggingModule {
    private final String MDC_KEY = "SQL_START";

    @Pointcut("execution(* com.mile.*.repository..*.*(..))")
    public void sqlLoggingPoint() {
    }

    @Around("sqlLoggingPoint()")
    public Object putSqlStartingPoint(final ProceedingJoinPoint joinPoint) throws Throwable {
        MDC.put(MDC_KEY, "[ QUERY START -> " + joinPoint.getSignature().toShortString() + "]");

        return joinPoint.proceed();
    }
}
