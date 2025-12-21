package com.example.spring.app.configuration;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
@Schema(name = "Configuration")
public class ConfigurationDTO implements Serializable {
    private String oauthBaseUrl;
    private String oauthSignInUrl;
    private String oauthSignOutUrl;
    private String oauthSignInRedirectUrl;
    private String oauthSignOutRedirectUrl;
    private String stripePriceIdFree;
    private String stripePriceIdBasic;
    private String stripePriceIdPremium;
    private String stripeBillingPortalCode;
    private String publicPostHogKey;
    private String publicPostHogHost;
}
