package com.example.spring.security;

import com.example.spring.security.SessionFilter;
import com.example.spring.service.UserSessionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public SessionFilter sessionFilter(UserSessionService userSessionService) {
        return new SessionFilter(userSessionService);
    }
}