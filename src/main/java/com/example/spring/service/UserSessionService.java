package com.example.spring.service;

import com.example.spring.model.UserSession;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserSessionService {

    // get all UserSessions
    List<UserSession> getAllUserSessions();

    // create UserSession rest api
    void saveUserSession(UserSession UserSession);

    // get UserSession by id rest api
    Optional<UserSession> getUserSessionById(Long id);

    // get UserSession by email rest api
    UserSession getUserSessionBySessionID(String sessionID);

    // update UserSession rest api
    ResponseEntity<UserSession> updateUserSession(Long id, UserSession userSession);

    // delete UserSession rest api
    void deleteUserSession(Long id);

    // delete all UserSessions rest api
    ResponseEntity<Map<String, Boolean>> deleteAllUserSessions();

    // check if session is valid
    Optional<UserSession> isValidSession(String sessionID);

}
