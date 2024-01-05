package com.example.spring.controller.DTO;

import lombok.Data;
import lombok.NonNull;

@Data
public class LoginDTO {

    @NonNull
    public String email;

    @NonNull
    public String password;
}