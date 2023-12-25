package com.example.spring.controller.Stripe;

import com.example.spring.exception.Exception;
import com.example.spring.model.StripeUser;
import com.example.spring.repository.StripeUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class StripeUserController {

    @Autowired
    private StripeUserRepository StripeUserRepository;

    // get all Users
    @GetMapping("/StripeUsers")
    public List<StripeUser> getAllStripeUsers(){
        return StripeUserRepository.findAll();
    }

    // create StripeUser rest api
    @PostMapping("/StripeUsers")
    public StripeUser createStripeUser(@RequestBody StripeUser StripeUser) {
        return StripeUserRepository.save(StripeUser);
    }

    // get StripeUser by id rest api
    @GetMapping("/StripeUsers/{id}")
    public ResponseEntity<StripeUser> getStripeUserById(@PathVariable Long id) {
        StripeUser StripeUser = StripeUserRepository.findById(id)
                .orElseThrow(() -> new Exception.ResourceNotFoundException("StripeUser not exist with id :" + id));
        return ResponseEntity.ok(StripeUser);
    }

    // update StripeUser rest api

    @PutMapping("/StripeUsers/{id}")
    public ResponseEntity<StripeUser> updateStripeUser(@PathVariable Long id, @RequestBody StripeUser StripeUserDetails){
        StripeUser StripeUser = StripeUserRepository.findById(id)
                .orElseThrow(() -> new Exception.ResourceNotFoundException("StripeUser not exist with id :" + id));

        StripeUser.setStripe_id(StripeUserDetails.getStripe_id());

        StripeUser updatedStripeUser = StripeUserRepository.save(StripeUser);
        return ResponseEntity.ok(updatedStripeUser);
    }

    // delete StripeUser rest api
    @DeleteMapping("/StripeUsers/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteStripeUser(@PathVariable Long id){
        StripeUser StripeUser = StripeUserRepository.findById(id)
                .orElseThrow(() -> new Exception.ResourceNotFoundException("StripeUser not exist with id :" + id));

        StripeUserRepository.delete(StripeUser);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }



}
