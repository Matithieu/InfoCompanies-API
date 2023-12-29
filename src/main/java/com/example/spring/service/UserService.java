package com.example.spring.service;

import com.example.spring.model.User;
import com.example.spring.model.UserSession;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserByEmail(String email);

    boolean hasUserAnEmail(String email);

    User validateAndGetUserByEmail(String email);

    User saveUser(User user);

    void deleteUser(User user);

    User validEmailAndPassword(String email, String password);

    void setSessionId(User user);
}
