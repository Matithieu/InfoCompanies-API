package com.example.spring.repository;

import com.example.spring.model.UserQuota;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserQuotaRepository extends JpaRepository<UserQuota, String> {
    Optional<UserQuota> findByUserId(String userId);
}
