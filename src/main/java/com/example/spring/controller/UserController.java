package com.example.spring.controller;

import com.example.spring.DTO.User;
import com.example.spring.keycloakClient.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
