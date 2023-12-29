package com.example.spring.security;

import com.example.spring.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    String FRONTEND_URL = "http://localhost:5173";

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Autowired
    private SessionFilter sessionFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionFixation().migrateSession()
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                        .expiredUrl(FRONTEND_URL + "/login")
                )
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.GET, "/api/v1/users", "/api/v1/user/**").hasAnyAuthority(ADMIN, USER)
                        .requestMatchers(HttpMethod.POST, "/subscriptions/trial").hasAnyAuthority(ADMIN, USER)

                        .requestMatchers(HttpMethod.POST, "/api/v1/user/**").hasAuthority(ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/v1/user/**").hasAuthority(ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/user/**").hasAuthority(ADMIN)

                        .requestMatchers("/auth/login", "/auth/register").permitAll()
                        .requestMatchers("/", "/error").permitAll()
                        .anyRequest().authenticated())

                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(sessionFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
}