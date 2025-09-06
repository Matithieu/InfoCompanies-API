package com.example.spring.controller;

import com.example.spring.config.EnvConfig;
import com.example.spring.dto.Configuration;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigurationController {

    private final EnvConfig envConfig;

    public ConfigurationController(EnvConfig envConfig) {
        this.envConfig = envConfig;
    }

    @Cacheable(value = "configCache", unless = "#result == null")
    @GetMapping("/config")
    public Configuration getEnv() {
        return Configuration.builder()
                .oauthBaseUrl(envConfig.getOAUTH_BASE_URL())
                .oauthSignInUrl(envConfig.getOAUTH_SIGN_IN_URL())
                .oauthSignOutUrl(envConfig.getOAUTH_SIGN_OUT_URL())
                .oauthSignInRedirectUrl(envConfig.getOAUTH_SIGN_IN_REDIRECT_URL())
                .oauthSignOutRedirectUrl(envConfig.getOAUTH_SIGN_OUT_REDIRECT_URL())
                .stripePriceIdFree(envConfig.getSTRIPE_PRICE_ID_FREE())
                .stripePriceIdBasic(envConfig.getSTRIPE_PRICE_ID_BASIC())
                .stripePriceIdPremium(envConfig.getSTRIPE_PRICE_ID_PREMIUM())
                .stripeBillingPortalCode(envConfig.getSTRIPE_BILLING_PORTAL_CODE())
                .build();
    }
}
