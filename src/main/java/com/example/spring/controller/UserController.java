package com.example.spring.controller;

import com.example.spring.DTO.User;
import com.example.spring.keycloakClient.UserResource;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static com.example.spring.security.JwtUtils.getClaimFromJwt;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")

public class UserController {

    @Autowired
    UserResource userResource;

    @GetMapping("/user")
    public User getUser() {
        String email = getClaimFromJwt("email");
        return userResource.getUserByEmail(email);
    }

    @PutMapping("/update-user")
    public void updateUser(@RequestBody User user) {
        if(user.getEmail() != null) {
            String email = getClaimFromJwt("email");
            if(Objects.equals(user.getEmail(), email)) {
                userResource.updateUser(user);
            }
        }
    }
}
