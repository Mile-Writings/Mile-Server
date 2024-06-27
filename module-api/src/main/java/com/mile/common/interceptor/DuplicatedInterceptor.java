package com.mile.common.interceptor;

import com.mile.common.utils.ContextHolderUtil;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.TooManyRequestException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@RequiredArgsConstructor
public class DuplicatedInterceptor implements HandlerInterceptor {

    private static final String REDIS_KEY = "MILE_REDIS";
    private static final String RMAP_VALUE = "MILE";
    private final RedissonClient redissonClient;
    private final ContextHolderUtil contextHolderUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (lock()) return true;
        throw new TooManyRequestException(ErrorMessage.TOO_MANY_REQUESTS_EXCEPTION);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        unlock();
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        unlock();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    private String getRmapKey() {
        return contextHolderUtil.getUserIdFromContextHolder();
    }

    private boolean lock() {
        final String rmapKey = getRmapKey();
        RMap<String, String> redissonClientMap = redissonClient.getMap(REDIS_KEY);
        return redissonClientMap.putIfAbsent(rmapKey, RMAP_VALUE) == null;
    }

    private void unlock() {
        final String rmapKey = getRmapKey();
        RMap<String, String> redissonClientMap = redissonClient.getMap(REDIS_KEY);
        redissonClientMap.remove(rmapKey);
    }
}

