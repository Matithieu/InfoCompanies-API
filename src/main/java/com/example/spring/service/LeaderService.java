package com.example.spring.service;

import com.example.spring.model.Leader;
import com.example.spring.repository.LeaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LeaderService {

    @Autowired
    private LeaderRepository leaderRepository;

    public Leader getLeaderById(Long id) {
        return leaderRepository.findLeaderById(id);
    }

    public List<Leader> getLeadersBySirens(String siren) {
        return leaderRepository.findAllBySiren(siren);
    }

    public Page<Leader> getLeadersByFirstAndLastName(String firstName, String lastName, Pageable pageable) {
        return leaderRepository.findByFirstNameLikeAndLastNameLike(firstName, lastName, pageable);
    }
}