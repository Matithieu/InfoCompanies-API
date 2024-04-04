package com.example.spring.keycloakclient;

import java.util.*;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring.DTO.User;
import com.example.spring.DTO.Role;
import com.example.spring.security.KeycloakSecurityUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.ws.rs.core.Response;

@RestController
@RequestMapping("/keycloak")
@SecurityRequirement(name = "Keycloak")
public class UserResource {
	
	@Autowired
	KeycloakSecurityUtil keycloakUtil;
	
	@Value("${realm}")
	private String realm;
	
	@GetMapping
	@RequestMapping("/users")
	public List<User> getUsers() {
		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
		List<UserRepresentation> userRepresentations = keycloak.realm(realm).users().list();
		return mapUsers(userRepresentations);
    }
	
	@GetMapping(value = "/users/{id}")
	public User getUser(@PathVariable("id") String id) {
		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
		return mapUser(keycloak.realm(realm).users().get(id).toRepresentation());
	}

	@GetMapping(value = "/users/{email}")
	public User getUserByEmail(@PathVariable("email") String email) {
		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
		List<UserRepresentation> userRepresentations = keycloak.realm(realm).users().search(email);
		if(CollectionUtil.isNotEmpty(userRepresentations)) {
			return mapUser(userRepresentations.get(0));
		}
		return null;
	}
	
	@PostMapping(value = "/user")
	public Response createUser(User user) {
		UserRepresentation userRep = mapUserRep(user);
		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
		Response res = keycloak.realm(realm).users().create(userRep);
		return Response.ok(user).build();
	}
	
	@PutMapping(value = "/user")
	public Response updateUser(User user) {
		UserRepresentation userRep = mapUserRep(user);
		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
		keycloak.realm(realm).users().get(user.getId()).update(userRep);
		return Response.ok(user).build();
	}
	
	@DeleteMapping(value = "/users/{id}")
	public Response deleteUser(@PathVariable("id") String id) {
		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
		keycloak.realm(realm).users().delete(id);
		return Response.ok().build();
	}
	
	@GetMapping(value = "/users/{id}/roles")
	public List<Role> getRoles(@PathVariable("id") String id) {
		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
		return RoleResource.mapRoles(keycloak.realm(realm).users()
				.get(id).roles().realmLevel().listAll());
	}

	@PostMapping(value = "/users/{id}/roles/{roleName}")
	public Response createRole(@PathVariable("id") String id, 
			@PathVariable("roleName") String roleName) {
		Keycloak keycloak = keycloakUtil.getKeycloakInstance();
		RoleRepresentation role = keycloak.realm(realm).roles().get(roleName).toRepresentation();
		keycloak.realm(realm).users().get(id).roles().realmLevel().add(Arrays.asList(role));
		return Response.ok().build();
	}

	private List<User> mapUsers(List<UserRepresentation> userRepresentations) {
		List<User> users = new ArrayList<>();
		if(CollectionUtil.isNotEmpty(userRepresentations)) {
			userRepresentations.forEach(userRep -> {
				users.add(mapUser(userRep));
			});
		}
		return users;
	}
	
	private User mapUser(UserRepresentation userRep) {
		User user = new User();
		user.setId(userRep.getId());
		user.setFirstName(userRep.getFirstName());
		user.setLastName(userRep.getLastName());
		user.setEmail(userRep.getEmail());
		user.setUserName(userRep.getUsername());
		user.setPhone(getAttributeValue(userRep.getAttributes(), "phone"));
		user.setQuota(getAttributeValue(userRep.getAttributes(), "quota"));
		user.setStreet(getAttributeValue(userRep.getAttributes(), "street"));
		user.setLocality(getAttributeValue(userRep.getAttributes(), "locality"));
		user.setRegion(getAttributeValue(userRep.getAttributes(), "region"));
		user.setPostalCode(getAttributeValue(userRep.getAttributes(), "postalCode"));
		user.setCountry(getAttributeValue(userRep.getAttributes(), "country"));
		user.setVerified(convertStringToBoolean(getAttributeValue(userRep.getAttributes(), "isVerified")));
		return user;
	}

	private UserRepresentation mapUserRep(User user) {
		UserRepresentation userRep = new UserRepresentation();
		userRep.setId(user.getId());
		userRep.setUsername(user.getUserName());
		userRep.setFirstName(user.getFirstName());
		userRep.setLastName(user.getLastName());
		userRep.setEmail(user.getEmail());
		userRep.setAttributes(updateAttributes(userRep.getAttributes(), "quota", user.getQuota()));
		userRep.setAttributes(updateAttributes(userRep.getAttributes(), "phone", user.getPhone()));
		userRep.setAttributes(updateAttributes(userRep.getAttributes(), "street", user.getStreet()));
		userRep.setAttributes(updateAttributes(userRep.getAttributes(), "locality", user.getLocality()));
		userRep.setAttributes(updateAttributes(userRep.getAttributes(), "region", user.getRegion()));
		userRep.setAttributes(updateAttributes(userRep.getAttributes(), "postalCode", user.getPostalCode()));
		userRep.setAttributes(updateAttributes(userRep.getAttributes(), "country", user.getCountry()));
		userRep.setAttributes(updateAttributes(userRep.getAttributes(), "isVerified", convertBooleanToString(user.isVerified())));
		userRep.setEnabled(true);
		userRep.setEmailVerified(true);
		List<CredentialRepresentation> creds = new ArrayList<>();
		CredentialRepresentation cred = new CredentialRepresentation();
		cred.setTemporary(false);
		cred.setValue(user.getPassword());
		creds.add(cred);
		userRep.setCredentials(creds);
		return userRep;
	}

	public boolean convertStringToBoolean(String value) {
		return value != null && value.equalsIgnoreCase("true");
	}

	public String convertBooleanToString(boolean value) {
		return value ? "true" : "false";
	}

	private Map<String, List<String>> updateAttributes(Map<String, List<String>> attributes, String key, String value) {
		if(attributes == null) {
			attributes = new HashMap<>();
		}
		attributes.put(key, Arrays.asList(value));
		return attributes;
	}

	private String getAttributeValue(Map<String, List<String>> attributes, String key) {
		if(attributes != null) {
			List<String> values = attributes.get(key);
			if(CollectionUtil.isNotEmpty(values)) {
				return values.get(0);
			}
		}
		return null;
	}
}