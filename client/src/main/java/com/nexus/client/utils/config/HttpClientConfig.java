package com.nexus.client.utils.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// This class is used to configure the RestTemplate bean
@Configuration
public class HttpClientConfig {

    // The RestTemplate bean is used to make HTTP requests to the server
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}