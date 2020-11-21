package com.putoet.api.web;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class HalMessageConverter {
    @Bean
    public HalMessageConverter createHalMessageConverter() {
        return new HalMessageConverter();
    }
}
