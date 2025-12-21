package com.example.spring.app.leader;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaderRepository extends JpaRepository<LeaderModel, Integer> {
    LeaderModel findLeaderById(Integer id);
    List<LeaderModel> findAllBySiren(String siren);
    Page<LeaderModel> findByFirstNameLikeAndLastNameLike(String firstName, String lastName, Pageable pageable);
}