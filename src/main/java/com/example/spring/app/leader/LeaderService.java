package com.example.spring.app.leader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LeaderService {

    @Autowired
    private LeaderRepository leaderRepository;

    public LeaderModel getLeaderById(Integer id) {
        return leaderRepository.findLeaderById(id);
    }

    public List<LeaderModel> getLeadersBySirens(String siren) {
        return leaderRepository.findAllBySiren(siren);
    }

    public Page<LeaderModel> getLeadersByFirstAndLastName(String firstName, String lastName, Pageable pageable) {
        return leaderRepository.findByFirstNameLikeAndLastNameLike(firstName, lastName, pageable);
    }
}