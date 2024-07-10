package com.example.spring.keycloakClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.spring.DTO.Role;
import com.example.spring.utils.KeycloakSecurityUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import jakarta.ws.rs.core.Response;

@RestController
public class RoleResource {

	@Autowired
	KeycloakSecurityUtil keycloakUtil;
	
	@Value("${realm}")
	private String realm;
	
	public List<Role> getRoles() {
		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
		List<RoleRepresentation> roleRepresentations = keycloak.realm(realm).roles().list();
		return mapRoles(roleRepresentations);
    }
	
	public Role getRole(@RequestBody String roleName) {
		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
		return mapRole(keycloak.realm(realm).roles().get(roleName).toRepresentation());
    }
	
	public Response createRole(Role role) {
		RoleRepresentation roleRep = mapRoleRep(role);
		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
		keycloak.realm(realm).roles().create(roleRep);
		return Response.ok(role).build();
	}
	
	public Response updateRole(Role role) {
		RoleRepresentation roleRep = mapRoleRep(role);
		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
		keycloak.realm(realm).roles().get(role.getName()).update(roleRep);
		return Response.ok(role).build();
	}
	
	public Response deleteUser(@RequestBody String roleName) {
		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
		keycloak.realm(realm).roles().deleteRole(roleName);
		return Response.ok().build();
	}

	public void addRoleToUser(@RequestBody String userId, @RequestBody String roleName) {
		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
		keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Collections.singletonList(keycloak.realm(realm).roles().get(roleName).toRepresentation()));
		Response.ok().build();
	}

	public void removeRoleFromUser(@RequestBody String userId, @RequestBody String roleName) {
		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
		keycloak.realm(realm).users().get(userId).roles().realmLevel().remove(Collections.singletonList(keycloak.realm(realm).roles().get(roleName).toRepresentation()));
		Response.ok().build();
	}
	
	public static List<Role> mapRoles(List<RoleRepresentation> representations) {
		List<Role> roles = new ArrayList<>();
		if(CollectionUtil.isNotEmpty(representations)) {
			representations.forEach(roleRep -> roles.add(mapRole(roleRep)));
		}
		return roles;
	}

	public static Role mapRole(RoleRepresentation roleRep) {
		Role role = new Role();
		role.setId(roleRep.getId());
		role.setName(roleRep.getName());
		return role;
	}
	
	public RoleRepresentation mapRoleRep(Role role) {
		RoleRepresentation roleRep = new RoleRepresentation();
		roleRep.setName(role.getName());
		return roleRep;
	}
}