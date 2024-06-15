package com.example.spring.repository;

import com.example.spring.model.CompanySeen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanySeenRepository extends JpaRepository<CompanySeen, Long> {
    Optional<CompanySeen> findByUserId(String userId);
}
