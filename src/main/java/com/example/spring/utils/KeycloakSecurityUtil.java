package com.example.spring.utils;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeycloakSecurityUtil {

    Keycloak keycloak;

    @Value("${KEYCLOAK_URL}")
    private String serverUrl;

    @Value("${KEYCLOAK_REALM}")
    private String realm;

    @Value("${KEYCLOAK_ADMIN_CLI_REALM}")
    private String clientId;

    @Value("${grant-type}")
    private String grantType;

    @Value("${KEYCLOAK_ADMIN_CLI}")
    private String username;

    @Value("${KEYCLOAK_ADMIN_CLI_PASSWORD}")
    private String password;

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
}