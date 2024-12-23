package com.mile.config;


import com.mile.common.filter.MDCLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<MDCLoggingFilter> thirdFilter() {
        FilterRegistrationBean<MDCLoggingFilter> filterRegistrationBean = new FilterRegistrationBean<>(
                new MDCLoggingFilter());
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }
}
