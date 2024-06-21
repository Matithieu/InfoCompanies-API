package com.example.spring.controller;

import com.example.spring.DTO.User;
import com.example.spring.keycloakClient.UserResource;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static com.example.spring.security.utils.SecurityUtils.parseEmailFromHeader;
import static com.example.spring.security.utils.SecurityUtils.parseUserFromHeader;

@CrossOrigin
@RestController
@RequestMapping("/v1/")

public class UserController {

    @Autowired
    UserResource userResource;

    @GetMapping("/user")
    public User getUser() {
        String userId = parseUserFromHeader();
        return userResource.getUserById(userId);
    }

    @PutMapping("/update-user")
    public Response updateUser(@RequestBody User user) {
        if(user.getEmail() != null) {
            String email = parseEmailFromHeader();
            if(Objects.equals(user.getEmail(), email)) {
                userResource.updateUser(user);
                return Response.ok().build();
            }
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
