package com.example.spring.controller;

import com.example.spring.controller.DTO.SignUpRequest;
import com.example.spring.exception.DuplicatedUserInfoException;
import com.example.spring.controller.DTO.LoginRequest;
import com.example.spring.model.User;
import com.example.spring.security.SecurityConfiguration;
import com.example.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// https://github.com/ivangfr/springboot-react-basic-auth/

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.validEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public User signUp(@RequestBody SignUpRequest signUpRequest) {
        if (userService.hasUserAnEmail(signUpRequest.getEmail())) {
            throw new DuplicatedUserInfoException(String.format("Email %s is already been used", signUpRequest.getEmail()));
        }

        return userService.saveUser(createUser(signUpRequest));
    }

    private User createUser(SignUpRequest signUpRequest) {
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setName(signUpRequest.getName());
        user.setRole(SecurityConfiguration.USER);
        return user;
    }
}