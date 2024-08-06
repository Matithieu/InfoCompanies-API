package com.example.spring.repository;

import com.example.spring.model.Leader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaderRepository extends JpaRepository<Leader, Long> {
    Leader findLeaderById(Long id);
    List<Leader> findAllBySiren(String siren);
    Page<Leader> findByFirstNameLikeAndLastNameLike(String firstName, String lastName, Pageable pageable);
}