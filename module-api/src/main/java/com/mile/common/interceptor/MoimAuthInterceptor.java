package com.mile.common.interceptor;

import com.mile.common.auth.JwtTokenProvider;
import com.mile.common.auth.annotation.UserAuthAnnotation;
import com.mile.common.utils.SecureUrlUtil;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.ForbiddenException;
import com.mile.exception.model.UnauthorizedException;
import com.mile.writername.domain.MoimRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.servlet.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

@Component
@RequiredArgsConstructor
public class MoimAuthInterceptor implements HandlerInterceptor {

    private static final String MOIM_ID = "moimId";
    private final JwtTokenProvider jwtTokenProvider;
    private final SecureUrlUtil secureUrlUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if(handler instanceof ResourceHttpRequestHandler) {
            return true;
        }
        HandlerMethod method = (HandlerMethod) handler;

        UserAuthAnnotation annotation = method.getMethodAnnotation(UserAuthAnnotation.class);

        if (annotation != null) {
            final String userToken = getUserTokenFromHeader(request);

            final HashMap<Long, MoimRole> roleFromUser = jwtTokenProvider.getJoinedRoleFromHeader(userToken);
            final Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE);

            return authenticateUserFromMap(annotation, roleFromUser, pathVariables);
        }
        return true;
    }

    private String getUserTokenFromHeader(final HttpServletRequest request) {
        final String userToken = request.getHeader("Authorization");

        if (userToken == null) {
            throw new UnauthorizedException(ErrorMessage.UN_LOGIN_EXCEPTION);
        }

        return userToken;
    }

    private boolean authenticateUserFromMap(final UserAuthAnnotation annotation,
                                            final Map<Long, MoimRole> userRoles,
                                            final Map<String, String> pathVariables) {
        switch (annotation.value()) {
            case OWNER -> {
                final Long requestMoimId = secureUrlUtil.decodeUrl(pathVariables.get(MOIM_ID));
                if (!userRoles.containsKey(requestMoimId) || !userRoles.get(requestMoimId).equals(MoimRole.OWNER)) {
                    throw new ForbiddenException(ErrorMessage.MOIM_OWNER_AUTHENTICATION_ERROR);
                }
                return true;
            }
            case WRITER_NAME -> {
                final String requestMoimId = secureUrlUtil.decodeUrl(pathVariables.get(MOIM_ID)).toString();
                if (!userRoles.containsKey(requestMoimId)) {
                    throw new ForbiddenException(ErrorMessage.USER_MOIM_AUTHENTICATE_ERROR);
                }
                return true;
            }
            case USER -> {
                return true;
            }
        }
        return true;
    }
}
