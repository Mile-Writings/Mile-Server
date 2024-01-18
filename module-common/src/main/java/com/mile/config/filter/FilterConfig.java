package com.mile.config.filter;


import com.mile.filter.CustomServletWrappingFilter;
import com.mile.filter.MDCFilter;
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
    public FilterRegistrationBean<MDCFilter> thirdFilter() {
        FilterRegistrationBean<MDCFilter> filterRegistrationBean = new FilterRegistrationBean<>(
                new MDCFilter());
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }
}
