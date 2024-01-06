package com.example.spring.service.userQuota;

import com.example.spring.model.UserQuota;

public interface UserQuotaService {

    boolean hasRemainingSearches(Long userId);

    void decrementRemainingSearches(Long userId);

    void saveUserQuota(UserQuota userQuota);
}