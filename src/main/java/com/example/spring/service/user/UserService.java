package com.example.spring.service.user;

import com.example.spring.controller.DTO.LoginDTO;
import com.example.spring.controller.DTO.RegisterDTO;
import com.example.spring.model.Role;
import com.example.spring.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserByEmail(String email);

    boolean hasUserAnEmail(String email);

    User saveUser(User user);

    void deleteUser(User user);

    User findByEmail(String email);

    ResponseEntity<?> register(RegisterDTO registerDto);

    ResponseEntity<?> authenticate(LoginDTO loginDto);

    Role saveRole(Role role);
}
