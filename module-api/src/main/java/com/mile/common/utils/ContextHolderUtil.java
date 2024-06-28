package com.mile.common.utils;

import com.mile.common.auth.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ContextHolderUtil {

    private final JwtTokenProvider jwtTokenProvider;

    public String getUserIdFromContextHolder() {
        HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return jwtTokenProvider.getUserFromJwt(servletRequest.getHeader("Authorization")).toString();
    }

}
