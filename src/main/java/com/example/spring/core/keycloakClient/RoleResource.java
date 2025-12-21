package com.example.spring.core.keycloakClient;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class RoleResource {

	@Autowired
	KeycloakInstance keycloakUtil;
	
	@Value("${KEYCLOAK_REALM}")
	private String realm;

	public void addRoleToUser(String userId, String roleName) {
		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
		keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Collections.singletonList(keycloak.realm(realm).roles().get(roleName).toRepresentation()));
		Response.ok().build();
	}

	public void removeRoleFromUser(String userId, String roleName) {
		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
		keycloak.realm(realm).users().get(userId).roles().realmLevel().remove(Collections.singletonList(keycloak.realm(realm).roles().get(roleName).toRepresentation()));
		Response.ok().build();
	}
}