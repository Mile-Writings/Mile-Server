package com.mile.common.filter;


import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class MDCLoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String requestId = UUID.randomUUID().toString();
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        MDC.put("request_id", requestId);
        MDC.put("path", httpRequest.getRequestURI());
        chain.doFilter(request, response);
        MDC.clear();
    }
}
