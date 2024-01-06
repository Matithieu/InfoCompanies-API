package com.example.spring.repository;

import com.example.spring.model.UserQuota;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQuotaRepository extends JpaRepository<UserQuota, Long> {
    UserQuota findUserQuotaById(Long id);
}