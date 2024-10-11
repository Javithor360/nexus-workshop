package com.nexus.client.config;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.context.annotation.Bean;

public class ThymeleafConfig {
    @Bean
    public LayoutDialect layoutDialect(){
        return new LayoutDialect();
    }
}
