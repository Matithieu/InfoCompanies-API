package com.example.spring.controller;

import com.example.spring.exception.Exception;
import com.example.spring.repository.UserSessionRepository;
import com.example.spring.model.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class UserSessionController {

    @Autowired
    private UserSessionRepository UserSessionRepository;

    // get all Users
    @GetMapping("/UserSessions")
    public List<UserSession> getAllUserSessions(){
        return UserSessionRepository.findAll();
    }

    // create UserSession rest api
    @PostMapping("/UserSessions")
    public UserSession createUserSession(@RequestBody UserSession UserSession) {
        return UserSessionRepository.save(UserSession);
    }

    // get UserSession by id rest api
    @GetMapping("/UserSessions/{id}")
    public ResponseEntity<UserSession> getUserSessionById(@PathVariable Long id) {
        UserSession UserSession = UserSessionRepository.findById(id)
                .orElseThrow(() -> new Exception.ResourceNotFoundException("UserSession not exist with id :" + id));
        return ResponseEntity.ok(UserSession);
    }

    // update UserSession rest api

    @PutMapping("/UserSessions/{id}")
    public ResponseEntity<UserSession> updateUserSession(@PathVariable Long id, @RequestBody UserSession UserSessionDetails){
        UserSession UserSession = UserSessionRepository.findById(id)
                .orElseThrow(() -> new Exception.ResourceNotFoundException("UserSession not exist with id :" + id));

        UserSession.setSession_id(UserSessionDetails.getSession_id());

        UserSession updatedUserSession = UserSessionRepository.save(UserSession);
        return ResponseEntity.ok(updatedUserSession);
    }

    // delete UserSession rest api
    @DeleteMapping("/UserSessions/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteUserSession(@PathVariable Long id){
        UserSession UserSession = UserSessionRepository.findById(id)
                .orElseThrow(() -> new Exception.ResourceNotFoundException("UserSession not exist with id :" + id));

        UserSessionRepository.delete(UserSession);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

}
