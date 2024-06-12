package com.example.spring.service;

import com.example.spring.model.UserQuota;
import com.example.spring.repository.UserQuotaRepository;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class UserQuotaService {

    private final UserQuotaRepository userQuotaRepository;
    private final Cache<String, UserQuota> quotaCache;
    private final Map<String, UserQuota> dirtyQuotas;

    @Autowired
    public UserQuotaService(UserQuotaRepository userQuotaRepository) {
        this.userQuotaRepository = userQuotaRepository;
        this.quotaCache = Caffeine.newBuilder()
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
        this.dirtyQuotas = new ConcurrentHashMap<>();
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
        dirtyQuotas.put(userId, userQuota); // Mark as dirty
        quotaCache.put(userId, userQuota);
    }

    @Scheduled(fixedRate = 300000) // Every 5 minutes
    public void flushQuotas() {
        if (!dirtyQuotas.isEmpty()) {
            userQuotaRepository.saveAll(dirtyQuotas.values());
            dirtyQuotas.clear();
        }
    }

    @PreDestroy
    public void onShutdown() {
        flushQuotas();
    }

    @PostConstruct
    public void init() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::onShutdown));
    }
}
