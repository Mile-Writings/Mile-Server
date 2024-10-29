package com.mile.config;


import com.mile.common.filter.MDCLoggingFilter;
import com.mile.exception.log.filter.CustomServletWrappingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<CustomServletWrappingFilter> secondFilter() {
        FilterRegistrationBean<CustomServletWrappingFilter> filterRegistrationBean = new FilterRegistrationBean<>(
                new CustomServletWrappingFilter());
        filterRegistrationBean.setOrder(0);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<MDCLoggingFilter> thirdFilter() {
        FilterRegistrationBean<MDCLoggingFilter> filterRegistrationBean = new FilterRegistrationBean<>(
                new MDCLoggingFilter());
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }
}
