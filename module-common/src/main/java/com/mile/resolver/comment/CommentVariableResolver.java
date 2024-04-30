package com.mile.resolver.comment;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.utils.SecureUrlUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;


@Component
@RequiredArgsConstructor
public class CommentVariableResolver implements HandlerMethodArgumentResolver {
    private static final String COMMENT_PATH_VARIABLE = "commentId";
    private final SecureUrlUtil secureUrlUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CommentIdPathVariable.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        final Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        final String id = pathVariables.get(COMMENT_PATH_VARIABLE);
        try {
            return secureUrlUtil.decodeUrl(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException(ErrorMessage.INVALID_URL_EXCEPTION);
        }
    }
}

