package com.example.spring.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class Configuration implements Serializable {
    private String oauthBaseUrl;
    private String oauthSignInUrl;
    private String oauthSignOutUrl;
    private String oauthSignInRedirectUrl;
    private String oauthSignOutRedirectUrl;
    private String stripePriceIdFree;
    private String stripePriceIdBasic;
    private String stripePriceIdPremium;
    private String stripeBillingPortalCode;
}
