package com.example.spring.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "")
@Validated
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EnvConfig {
    @Value("${OAUTH_BASE_URL}") private String OAUTH_BASE_URL;
    @Value("${OAUTH_SIGN_IN_URL}") private String OAUTH_SIGN_IN_URL;
    @Value("${OAUTH_SIGN_OUT_URL}") private String OAUTH_SIGN_OUT_URL;
    @Value("${OAUTH_SIGN_IN_REDIRECT_URL}") private String OAUTH_SIGN_IN_REDIRECT_URL;
    @Value("${OAUTH_SIGN_OUT_REDIRECT_URL}") private String OAUTH_SIGN_OUT_REDIRECT_URL;
    @Value("${STRIPE_PRICE_ID_FREE}") private String STRIPE_PRICE_ID_FREE;
    @Value("${STRIPE_PRICE_ID_BASIC}") private String STRIPE_PRICE_ID_BASIC;
    @Value("${STRIPE_PRICE_ID_PREMIUM}") private String STRIPE_PRICE_ID_PREMIUM;
    @Value("${STRIPE_BILLING_PORTAL_CODE}") private String STRIPE_BILLING_PORTAL_CODE;
    @Value("${PUBLIC_POSTHOG_KEY:}") private String PUBLIC_POSTHOG_KEY;
    @Value("${PUBLIC_POSTHOG_HOST:}") private String PUBLIC_POSTHOG_HOST;
}
