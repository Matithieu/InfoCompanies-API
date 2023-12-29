package com.example.spring.service;

import com.example.spring.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();

    User getUserByEmail(String email);

    boolean hasUserAnEmail(String email);

    User validateAndGetUserByEmail(String email);

    User saveUser(User user);

    void deleteUser(User user);

    User validEmailAndPassword(String email, String password);

}
