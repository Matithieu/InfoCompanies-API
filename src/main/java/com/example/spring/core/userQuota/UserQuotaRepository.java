package com.example.spring.core.userQuota;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserQuotaRepository extends JpaRepository<UserQuotaModel, String> {
    Optional<UserQuotaModel> findByUserId(String userId);
}
