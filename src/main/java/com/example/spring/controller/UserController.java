package com.example.spring.controller;

import com.example.spring.model.User;
import com.example.spring.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class UserController {

    @Autowired
    private UserService userService;

    // get User by id rest api
    @GetMapping("/user/{email}")
    public ResponseEntity<User> getUserEmail(@PathVariable String email) {
        User User = userService.getUserByEmail(email);
        return ResponseEntity.ok(User);
    }

    // get all users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}