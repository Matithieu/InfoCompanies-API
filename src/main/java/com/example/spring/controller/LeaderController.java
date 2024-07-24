package com.example.spring.controller;

import com.example.spring.model.Leader;
import com.example.spring.service.LeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/v1/leader")
public class LeaderController {

    @Autowired
    private LeaderService leaderService;

    // Example: http://localhost:8080/api/v1/leader/get-by-id/123
    @GetMapping("/get-by-id/{id}")
    public Leader getLeaderById(@PathVariable("id") Long id) {
        return leaderService.getLeaderById(id);
    }

    // Example: http://localhost:8080/api/v1/leader/get-by-siren?siren=1234&page=0
    @GetMapping("/get-by-siren")
    public Leader getLeaderBySiren(@RequestParam("siren") String siren) {
        return leaderService.getLeaderBySiren(siren);
    }

    // Example: http://localhost:8080/api/v1/leader/get-by-first-and-last-name?firstName=exemple&lastName=exemple&page=0
    @GetMapping("/get-by-first-and-last-name")
    public Page<Leader> getLeadersByName(@RequestParam("firstName") String firstName,
                                         @RequestParam("lastName") String lastName,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return leaderService.getLeadersByFirstAndLastName(firstName, lastName, pageable);
    }
}
