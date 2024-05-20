package com.example.spring.controller;

import com.example.spring.DTO.User;
import com.example.spring.keycloakClient.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static com.example.spring.security.utils.SecurityUtils.parseEmailFromHeader;

@CrossOrigin
@RestController
@RequestMapping("/v1/")

public class UserController {

    @Autowired
    UserResource userResource;

    @GetMapping("/user")
    public User getUser() {
        String email = parseEmailFromHeader();
        return userResource.getUserByEmail(email);
    }

    @PutMapping("/update-user")
    public void updateUser(@RequestBody User user) {
        if(user.getEmail() != null) {
            String email = parseEmailFromHeader();
            if(Objects.equals(user.getEmail(), email)) {
                userResource.updateUser(user);
            }
        }
    }
}
