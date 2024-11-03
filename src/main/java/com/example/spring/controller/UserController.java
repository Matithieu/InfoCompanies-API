package com.example.spring.controller;

import com.example.spring.DTO.User;
import com.example.spring.keycloakClient.UserResource;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.spring.utils.HeadersUtil.parseUserFromHeader;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")

public class UserController {

    @Autowired
    UserResource userResource;

    @GetMapping("/user")
    public User getUser() {
        String userId = parseUserFromHeader();
        return userResource.getUserById(userId);
    }

    @PostMapping("/completeOnboarding")
    public Response completeOnboarding() {
        String userId = parseUserFromHeader();
        userResource.completeOnboarding(userId);
        return Response.ok().build();
    }

    @PutMapping("/update-user")
    public Response updateUser(@RequestBody User user) {
        String id = parseUserFromHeader();
        User existingUser = userResource.getUserById(id);

        if (existingUser != null) {
            // Ensure that the user's ID and verified status are not changed
            user.setId(existingUser.getId());
            user.setVerified(existingUser.isVerified());
            user.setTier(existingUser.getTier());

            userResource.updateUser(user);
            return Response.ok().build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    /*
    @GetMapping("/register")
    public String registerEndpoint() {
        return userResource.returnRegistrationEndpoint();
    }
     */
}
