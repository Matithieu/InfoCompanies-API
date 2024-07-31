package com.example.spring.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class StripeConfig {
    @Value("${STRIPE_PRICE_ID_FREE}")
    private String stripePriceIdFree;

    @Value("${STRIPE_PRICE_ID_BASIC}")
    private String stripePriceIdBasic;

    @Value("${STRIPE_PRICE_ID_PREMIUM}")
    private String stripePriceIdPremium;
}
