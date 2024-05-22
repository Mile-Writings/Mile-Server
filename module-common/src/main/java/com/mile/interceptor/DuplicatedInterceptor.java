package com.mile.interceptor;

import com.mile.filter.wrapper.CachedBodyRequestWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
//import com.mile.authentication.PrincipalHandler;

@Component
@RequiredArgsConstructor
public class DuplicatedInterceptor implements HandlerInterceptor {

    private static final String REDIS_KEY = "MILE_REDIS";
    private static final String RMAP_VALUE = "MILE";
    private final RedissonClient redissonClient;
    private final PrincipalHandler principalHandler;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

    }

    private String getRmapKey(HttpServletRequest request) {
        final String body = ((CachedBodyRequestWrapper) request).getBody();
//        principalHandler.getU
    }

    private boolean lock(HttpSerletRequest request) {
        final String rmapKey = getRmapKey()
    }
}

