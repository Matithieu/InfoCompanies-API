package com.example.spring.controller;

import com.example.spring.controller.DTO.SignUpRequest;
import com.example.spring.exception.DuplicatedUserInfoException;
import com.example.spring.controller.DTO.LoginRequest;
import com.example.spring.model.User;
import com.example.spring.model.UserSession;
import com.example.spring.security.SecurityConfiguration;
import com.example.spring.service.UserService;
import com.example.spring.service.UserSessionService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Semaphore;

// https://github.com/ivangfr/springboot-react-basic-auth/

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Semaphore mutex = new Semaphore(1);

    @Autowired
    private UserService userService;

    @Autowired
    private UserSessionService userSessionService;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        try {
            mutex.acquire();
            User user = userService.validEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
            if (user != null) {

                if (user.getSessionId() != null) {
                    user.setSessionId(null);
                    userService.setSessionId(user);
                    //userSessionService.deleteUserSession(user.getSessionId().getId());
                }

                System.out.println("User connecting: " + user.getEmail());
                UserSession userSession = new UserSession();
                userSession.setId(userSession.createId());
                userSession.setSessionId(userSession.createSessionId());
                userSessionService.saveUserSession(userSession);

                user.setSessionId(userSession);
                userService.setSessionId(user);

                session.setAttribute("session_id", userSession.getId());
                System.out.println("session_id: " + session.getAttribute("session_id"));

                return ResponseEntity.ok(user);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } finally {
            mutex.release();
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public User signUp(@RequestBody SignUpRequest signUpRequest, HttpSession session) throws Exception {
        try {
            mutex.acquire();
            if (userService.hasUserAnEmail(signUpRequest.getEmail())) {
                throw new DuplicatedUserInfoException(String.format("Email %s is already been used", signUpRequest.getEmail()));
            }

            User user = createUser(signUpRequest);

            UserSession userSession = new UserSession();
            userSession.setId(userSession.createId());
            userSession.setSessionId(userSession.createSessionId());
            userSessionService.saveUserSession(userSession);

            user.setSessionId(userSession);
            userService.saveUser(user);

            session.setAttribute("session_id", userSession.getId());

            return userService.getUserByEmail(signUpRequest.getEmail());
        } catch (InterruptedException e) {
            throw new Exception("Error when creating the user ", e);
        } finally {
            mutex.release();
        }
    }

    private User createUser(SignUpRequest signUpRequest) {
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setName(signUpRequest.getName());
        user.setRole(SecurityConfiguration.USER);

        return user;
    }
}