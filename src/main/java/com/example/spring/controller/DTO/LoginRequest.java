package com.example.spring.controller.DTO;

import lombok.Data;
import lombok.NonNull;

@Data
public class LoginRequest {

    @NonNull
    private String email;

    @NonNull
    private String password;
}