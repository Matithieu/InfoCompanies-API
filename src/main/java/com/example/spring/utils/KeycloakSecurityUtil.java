package com.example.spring.utils;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeycloakSecurityUtil {

    Keycloak keycloak;

    @Value("${server-url}")
    private String serverUrl;

    @Value("${realm}")
    private String realm;

    @Value("${client-id}")
    private String clientId;

    @Value("${grant-type}")
    private String grantType;

    @Value("${name}")
    private String username;

    @Value("${password}")
    private String password;

    @Value("${REDIRECT_URI}")
    private String redirectUri;

    @Value("${REGISTER_CLIENT_ID}")
    private String registerClientId;

    @Value("${FULL_DOMAIN}")
    private String fullDomain;


    public Keycloak getKeycloakInstance() {
        if (keycloak == null) {
            keycloak = KeycloakBuilder.builder().
                    serverUrl(serverUrl)
                    .realm(realm)
                    .clientId(clientId)
                    .grantType(OAuth2Constants.PASSWORD)
                    .username(username)
                    .password(password)
                    .build();
        }
        return keycloak;
    }

	/*
	public String returnRegistrationPage() {
        return String.format(
				"%s/auth/realms/%s/protocol/openid-connect/registrations?client_id=%s&response_type=code&scope=openid&redirect_uri=%s",
				fullDomain,
				realm,
				registerClientId,
				redirectUri
		);
	}
	 */
}