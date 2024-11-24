package com.example.spring.security;

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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Landing page
                        .requestMatchers(HttpMethod.GET, "/api/v1/company/landing-filter").permitAll()
                        // Company
                        .requestMatchers("/api/v1/company/**").hasRole("verified")
                        .requestMatchers("/api/v1/companies-status/**").hasRole("verified")
                        // Stripe
                        .requestMatchers(HttpMethod.POST, "/api/v1/stripe/webhook").permitAll()
                        // AutoComplete
                        .requestMatchers(HttpMethod.GET, "/api/v1/autocomplete/industry-sector").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/autocomplete/city").permitAll()
                        .anyRequest().permitAll())

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
