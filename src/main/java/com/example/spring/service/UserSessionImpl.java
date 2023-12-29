package com.example.spring.service;

import com.example.spring.model.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserSessionImpl implements UserSessionService {

    @Autowired
    private UserSessionService userSessionService;

    @Override
    public List<UserSession> getAllUserSessions() {
        return userSessionService.getAllUserSessions();
    }

    @Override
    public UserSession createUserSession(UserSession UserSession) {
        return null;
    }

    @Override
    public ResponseEntity<UserSession> getUserSessionById(Long id) {
        return null;
    }

    @Override
    public UserSession getUserSessionBySessionID(String sessionID) {
        return null;
    }

    @Override
    public ResponseEntity<UserSession> updateUserSession(Long id, UserSession userSession) {
        return null;
    }

    @Override
    public ResponseEntity<Map<String, Boolean>> deleteUserSession(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<Map<String, Boolean>> deleteAllUserSessions() {
        return null;
    }
}