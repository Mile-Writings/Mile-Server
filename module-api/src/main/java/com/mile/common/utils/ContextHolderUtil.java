package com.mile.common.utils;

import com.mile.common.auth.JwtTokenProvider;
import com.mile.common.auth.JwtValidationType;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ContextHolderUtil {

    private final JwtTokenProvider jwtTokenProvider;

    public String getUserIdFromContextHolder() {
        HttpServletRequest servletRequest = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        if (HttpMethod.OPTIONS.matches(servletRequest.getMethod())) {
            return null;
        }

        final String token = servletRequest.getHeader("Authorization");

        if(!jwtTokenProvider.validateToken(token).equals(JwtValidationType.VALID_JWT)) {
            throw new UnauthorizedException(ErrorMessage.TOKEN_VALIDATION_ERROR);
        }

        return jwtTokenProvider.getUserFromAuthHeader(token).toString();
    }

}
