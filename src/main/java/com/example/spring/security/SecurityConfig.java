package com.example.spring.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> {
                    // Public endpoints are enabled through the OAuth proxy with allowed endpoints
                    // Landing page
                    authorize.requestMatchers(HttpMethod.GET, "/v1/company/landing-filter").permitAll();
                    // AutoComplete
                    authorize.requestMatchers(HttpMethod.GET, "/v1/autocomplete/industry-sector").permitAll();
                    authorize.requestMatchers(HttpMethod.GET, "/v1/autocomplete/city").permitAll();
                    // Stripe
                    authorize.requestMatchers(HttpMethod.POST, "/v1/stripe/webhook").permitAll();
                    // Health check
                    authorize.requestMatchers(HttpMethod.GET, "/actuator/health").permitAll();

                    // Private endpoints are protected by the JWT authentication filter
                    // Company
                    authorize.requestMatchers("/v1/company/**").hasRole("verified");
                    authorize.requestMatchers("/v1/companies-status/**").hasRole("verified");
                    // Leader
                    authorize.requestMatchers("/v1/leader/**").hasRole("verified");

                    // Swagger + OpenAPI - only in dev
                    if ("dev".equals(activeProfile)) {
                        // To access Swagger UI and OpenAPI documentation on Docker
                        // Fill the search bar at the top with: https://localhost/api/v3/api-docs
                        authorize.requestMatchers("/swagger-ui/**", "/api/v3/api-docs").hasRole("verified");
                    }

                    // permitAll() is used here because the OAuth proxy handles the authentication
                    // and the user is already authenticated before reaching this point.
                    // This means that all requests are allowed, but the user must be authenticated
                    authorize.anyRequest().permitAll();
                })
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
}
