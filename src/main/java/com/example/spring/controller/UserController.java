package com.example.spring.controller;

import com.example.spring.exception.Exception;
import com.example.spring.model.User;
import com.example.spring.repository.UserRepository;
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
    private UserRepository UserRepository;

    // get all Users
    @GetMapping("/Users")
    public List<User> getAllUsers(){
        return UserRepository.findAll();
    }

    // create User rest api
    @PostMapping("/Users")
    public User createUser(@RequestBody User User) {
        return UserRepository.save(User);
    }

    // get User by id rest api
    @GetMapping("/Users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User User = UserRepository.findById(id)
                .orElseThrow(() -> new Exception.ResourceNotFoundException("User not exist with id :" + id));
        return ResponseEntity.ok(User);
    }

    // update User rest api

    @PutMapping("/Users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User UserDetails){
        User User = UserRepository.findById(id)
                .orElseThrow(() -> new Exception.ResourceNotFoundException("User not exist with id :" + id));

        User.setName(UserDetails.getName());
        User.setEmail(UserDetails.getEmail());
        User.setPassword(UserDetails.getPassword());
        User.setPhone(UserDetails.getPhone());
        User.setCity(UserDetails.getCity());
        User.setAddress(UserDetails.getAddress());
        User.setRole(UserDetails.getRole());
        User.setStripe_api(UserDetails.getStripe_api());
        User.setSession_id(UserDetails.getSession_id());

        User updatedUser = UserRepository.save(User);
        return ResponseEntity.ok(updatedUser);
    }

    // delete User rest api
    @DeleteMapping("/Users/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Long id){
        User User = UserRepository.findById(id)
                .orElseThrow(() -> new Exception.ResourceNotFoundException("User not exist with id :" + id));

        UserRepository.delete(User);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

}
