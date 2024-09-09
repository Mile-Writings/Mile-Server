package com.mile.common.resolver.reply;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.common.utils.SecureUrlUtil;
import com.mile.exception.model.NotFoundException;
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
public class ReplyVariableResolver implements HandlerMethodArgumentResolver {
    private static final String REPLY_PATH_VARIABLE = "replyId";
    private final SecureUrlUtil secureUrlUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ReplyIdPathVariable.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        final Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        final String replyId = pathVariables.get(REPLY_PATH_VARIABLE);
        System.out.println(replyId);
        try {
            return secureUrlUtil.decodeUrl(replyId);
        } catch (NumberFormatException e) {
            throw new NotFoundException(ErrorMessage.INVALID_URL_EXCEPTION);
        }
    }
}