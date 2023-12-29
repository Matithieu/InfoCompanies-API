package com.example.spring.controller;

import com.example.spring.exception.Exception;
import com.example.spring.model.User;
import com.example.spring.repository.UserRepository;
import com.example.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class UserController {

    @Autowired
    private UserService userService;

    // get all Users
    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    // create User rest api
    @PostMapping("/user")
    public User createUser(@RequestBody User User) {
        return userService.saveUser(User);
    }

    // get User by id rest api
    @GetMapping("/user/{email}")
    public ResponseEntity<User> getUserEmail(@PathVariable String email) {
        User User = userService.getUserByEmail(email);
        return ResponseEntity.ok(User);
    }

    // get User by email rest api
    public User getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }

    // update User rest api
    @PutMapping("/user/{email}")
    public ResponseEntity<User> updateUser(@PathVariable String email, @RequestBody User UserDetails){
        User User = userService.getUserByEmail(email);

        User.setName(UserDetails.getName());
        User.setEmail(UserDetails.getEmail());
        User.setPassword(UserDetails.getPassword());
        User.setPhone(UserDetails.getPhone());
        User.setCity(UserDetails.getCity());
        User.setAddress(UserDetails.getAddress());
        User.setRole(UserDetails.getRole());
        User.setVerified(UserDetails.isVerified());
        User.setStripeId(UserDetails.getStripeId());
        User.setSessionId(UserDetails.getSessionId());

        User updatedUser = userService.saveUser(User);
        return ResponseEntity.ok(updatedUser);
    }

    // delete User rest api
    @DeleteMapping("/user/{email}")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable String email){
        User User = userService.getUserByEmail(email);

        userService.deleteUser(User);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}