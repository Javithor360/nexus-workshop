package com.nexus.client.utils.security;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate() {
            @Override
            public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null && auth.isAuthenticated()) {
                    String username = auth.getName();
                    String password = (String) auth.getCredentials();
                    return new RestTemplateBuilder()
                            .basicAuthentication(username, password)
                            .build()
                            .getForObject(url, responseType, uriVariables);
                }
                return super.getForObject(url, responseType, uriVariables);
            }
        };
    }
}
