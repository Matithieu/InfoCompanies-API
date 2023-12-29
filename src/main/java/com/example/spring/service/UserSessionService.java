package com.example.spring.service;

import com.example.spring.model.UserSession;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserSessionService {

        // get all UserSessions
        public List<UserSession> getAllUserSessions();

        // create UserSession rest api
        public UserSession createUserSession(UserSession UserSession);

        // get UserSession by id rest api
        public ResponseEntity<UserSession> getUserSessionById(Long id);

        // get UserSession by email rest api
        public UserSession getUserSessionBySessionID(String sessionID);

        // update UserSession rest api
        public ResponseEntity<UserSession> updateUserSession(Long id, UserSession userSession);

        // delete UserSession rest api
        public ResponseEntity<Map<String, Boolean>> deleteUserSession(Long id);

        // delete all UserSessions rest api
        public ResponseEntity<Map<String, Boolean>> deleteAllUserSessions();
}
