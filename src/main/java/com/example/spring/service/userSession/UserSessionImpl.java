package com.example.spring.service.userSession;

import com.example.spring.model.UserSession;
import com.example.spring.repository.UserSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserSessionImpl implements UserSessionService {

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Override
    public List<UserSession> getAllUserSessions() {
        return userSessionRepository.findAll();
    }

    @Override
    public void saveUserSession(UserSession UserSession) {
        userSessionRepository.save(UserSession);
    }

    @Override
    public Optional<UserSession> getUserSessionById(Long id) {
        return userSessionRepository.findById(id);
    }

    @Override
    public UserSession getUserSessionBySessionID(String sessionID) {
        return userSessionRepository.findBySessionId(sessionID);
    }

    @Override
    public ResponseEntity<UserSession> updateUserSession(Long id, UserSession userSession) {
        UserSession userSessionToUpdate = userSessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserSession not exist with id: " + id));
        userSessionToUpdate.setSessionId(userSession.getSessionId());
        userSessionToUpdate.setId(userSession.getId());
        UserSession updatedUserSession = userSessionRepository.save(userSessionToUpdate);
        return ResponseEntity.ok(updatedUserSession);
    }

    @Override
    public void deleteUserSession(Long id) {
        userSessionRepository.deleteById(id);
    }

    @Override
    public ResponseEntity<Map<String, Boolean>> deleteAllUserSessions() {
        userSessionRepository.deleteAll();
        return null;
    }

    public Optional<UserSession> isValidSession(String sessionID) {
        return userSessionRepository.findById(Long.parseLong(sessionID));
    }
}