package com.example.spring.controller.DTO;

import lombok.Data;
import lombok.NonNull;

@Data
public class RegisterDTO {

    @NonNull
    private String password;

    @NonNull
    private String name;

    @NonNull
    private String email;
}