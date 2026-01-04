package com.example.spring.app.leader;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/v1/leaders")
public class LeaderController {

    private final LeaderService leaderService;

    public LeaderController(LeaderService leaderService) {
        this.leaderService = leaderService;
    }

    // Example: http://localhost:8080/api/v1/leader/get-by-id/123
    @GetMapping("/{id}")
    public LeaderModel getLeaderById(@PathVariable("id") Integer leaderId) {
        return leaderService.getLeaderById(leaderId);
    }

    // Example: http://localhost:8080/api/v1/leader/get-by-siren?siren=exemple
    @GetMapping("/by-siren/{siren}")
    public List<LeaderModel> getLeaderBySiren(@PathVariable String siren) {
        return leaderService.getLeadersBySirens(siren);
    }

    // Example: http://localhost:8080/api/v1/leader/get-by-first-and-last-name?firstName=exemple&lastName=exemple&page=0
    @GetMapping("/search")
    public Page<LeaderModel> getLeadersByName(@RequestParam("firstName") String firstName,
                                              @RequestParam("lastName") String lastName,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return leaderService.getLeadersByFirstAndLastName(firstName, lastName, pageable);
    }
}
