package com.mile.config;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.mile.external.client")
@ImportAutoConfiguration(FeignAutoConfiguration.class)
public class OpenFeignConfig {
}
