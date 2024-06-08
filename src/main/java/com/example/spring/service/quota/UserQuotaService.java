package com.example.spring.service.quota;

import com.example.spring.model.UserQuota;
import com.example.spring.repository.UserQuotaRepository;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserQuotaService {

    private final UserQuotaRepository userQuotaRepository;
    private final Cache<String, UserQuota> quotaCache;

    @Autowired
    public UserQuotaService(UserQuotaRepository userQuotaRepository) {
        this.userQuotaRepository = userQuotaRepository;
        this.quotaCache = Caffeine.newBuilder()
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
    }

    public UserQuota getQuotaForUser(String userId) {
        return quotaCache.get(userId, this::loadQuotaFromDatabase);
    }

    private UserQuota loadQuotaFromDatabase(String userId) {
        return userQuotaRepository.findByUserId(userId)
                .orElseGet(() -> {
                    UserQuota newUserQuota = new UserQuota();
                    newUserQuota.setUserId(userId);
                    newUserQuota.setQuotaAllocated(100); // Default quota allocation
                    newUserQuota.setQuotaUsed(0);
                    userQuotaRepository.save(newUserQuota);
                    return newUserQuota;
                });
    }

    public void updateQuotaForUser(String userId, int quotaUsed) {
        UserQuota userQuota = getQuotaForUser(userId);
        userQuota.setQuotaUsed(quotaUsed);
        userQuotaRepository.save(userQuota);
        quotaCache.put(userId, userQuota);
    }
}
