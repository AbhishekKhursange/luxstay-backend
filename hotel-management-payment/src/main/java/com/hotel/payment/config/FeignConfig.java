package com.hotel.payment.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor internalRequestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("X-Internal-Call", "true");
        };
    }
}