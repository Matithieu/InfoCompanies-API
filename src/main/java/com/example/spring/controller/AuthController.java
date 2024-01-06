package com.example.spring.controller;

import com.example.spring.controller.DTO.LoginDTO;
import com.example.spring.controller.DTO.RegisterDTO;
import com.example.spring.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Semaphore;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Semaphore mutex = new Semaphore(1);

    @Autowired
    private UserService userService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody LoginDTO loginDto) {
        return userService.authenticate(loginDto);
    }

    @PostMapping("/register")
    public ResponseEntity<?> signUp(@RequestBody RegisterDTO registerDto) throws Exception {
        try {
            mutex.acquire();
            return userService.register(registerDto);
        } finally {
            mutex.release();
        }
    }
}