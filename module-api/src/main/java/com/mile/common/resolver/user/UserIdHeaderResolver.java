package com.mile.common.resolver.user;

import com.mile.common.auth.JwtTokenProvider;
import com.mile.common.auth.JwtValidationType;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class UserIdHeaderResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserId.class) && Long.class.equals(parameter.getParameterType());
    }

    @Override
    public Long resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, @NotNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        final String token = request.getHeader("Authorization");

        if (!jwtTokenProvider.validateToken(token).equals(JwtValidationType.VALID_JWT)) {
            throw new UnauthorizedException(ErrorMessage.TOKEN_VALIDATION_ERROR);
        }

        return jwtTokenProvider.getUserFromJwt(token);
    }

}