package com.example.spring.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Value("${FULL_DOMAIN}")
    private String FULL_DOMAIN;

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(List.of(
                FULL_DOMAIN,
                "https://localhost:5173",
                "https://localhost",
                "http://localhost/**",
                "https://stripe.com/**",
                "https://checkout.stripe.com/**",
                "http://3.18.12.63",
                "http://3.130.192.231",
                "http://13.235.14.237",
                "http://13.235.122.149",
                "http://18.211.135.69",
                "http://35.154.171.200",
                "http://52.15.183.38",
                "http://54.88.130.119",
                "http://54.88.130.237",
                "http://54.187.174.169",
                "http://54.187.205.235",
                "http://54.187.216.72"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}