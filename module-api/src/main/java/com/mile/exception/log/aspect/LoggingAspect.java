package com.mile.exception.log.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mile.exception.log.filter.wrapper.CachedBodyRequestWrapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("execution(* com.mile.exception.handler.GlobalExceptionHandler.handleException*(..))")
    public void controllerErrorLevelExecute() {
    }
    @Pointcut("execution(* com.mile.controller..*(..)) || ( execution(* com.mile.exception.handler..*(..)) && !execution(* com.mile.exception.handler.GlobalExceptionHandler.handleException*(..)))")
    public void controllerInfoLevelExecute() {
    }

    @Around("com.mile.exception.log.aspect.LoggingAspect.controllerInfoLevelExecute()")
    public Object requestInfoLevelLogging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        final CachedBodyRequestWrapper cachedBodyRequestWrapper = new CachedBodyRequestWrapper(request);
        long startAt = System.currentTimeMillis();
        Object returnValue = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        long endAt = System.currentTimeMillis();

        log.info("================================================NEW===============================================");
        log.info("====> Request: {} {} ({}ms)\n *Header = {}", request.getMethod(), request.getRequestURL(), endAt - startAt, getHeaders(request));
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            log.info("====> Body: {}", objectMapper.readTree(cachedBodyRequestWrapper.getBody()));
        }
        if (returnValue != null) {
            log.info("====> Response: {}", returnValue);
        }
        log.info("================================================END===============================================");
        return returnValue;
    }

    @Around("com.mile.exception.log.aspect.LoggingAspect.controllerErrorLevelExecute()")
    public Object requestErrorLevelLogging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        final CachedBodyRequestWrapper cachedBodyRequestWrapper = new CachedBodyRequestWrapper(request);
        long startAt = System.currentTimeMillis();
        Object returnValue = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        long endAt = System.currentTimeMillis();

        log.error("====> Request: {} {} ({}ms)\n *Header = {}", request.getMethod(), request.getRequestURL(), endAt - startAt, getHeaders(request));
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            log.error("====> Body: {}", objectMapper.readTree(cachedBodyRequestWrapper.getBody()));
        }
        if (returnValue != null) {
            log.error("====> Response: {}", returnValue);
        }
        log.error("================================================END===============================================");
        return returnValue;
    }

    private Map<String, Object> getHeaders(HttpServletRequest request) {
        Map<String, Object> headerMap = new HashMap<>();

        Enumeration<String> headerArray = request.getHeaderNames();
        while (headerArray.hasMoreElements()) {
            String headerName = headerArray.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap;
    }
}