package com.example.spring.service;

import com.example.spring.model.UserQuota;
import com.example.spring.repository.UserQuotaRepository;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@EnableScheduling
@Service
public class UserQuotaService {

    private static final Logger LOGGER = Logger.getLogger(UserQuotaService.class.getName());

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

    public void createQuotaForUser(String userId, int quotaAllocated) {
        UserQuota userQuota = new UserQuota(userId, quotaAllocated, 0);
        userQuota.setLastResetDate(LocalDate.now()); // Set the last reset date to the current date
        userQuotaRepository.save(userQuota);
        quotaCache.put(userId, userQuota);
    }

    private UserQuota loadQuotaFromDatabase(String userId) {
        return userQuotaRepository.findByUserId(userId).orElse(null);
    }

    public synchronized void updateQuotaForUser(String userId, int quotaUsed) {
        UserQuota userQuota = getQuotaForUser(userId);
        userQuota.setQuotaUsed(quotaUsed);
        dirtyQuotas.put(userId, userQuota); // Mark as dirty
        quotaCache.put(userId, userQuota);
    }

    @Scheduled(fixedRate = 1000 * 60 * 3) // Every 3 minutes
    public void flushQuotas() {
        LOGGER.info("Scheduled task running...");
        if (!dirtyQuotas.isEmpty()) {
            LOGGER.info("Flushing quotas...");
            userQuotaRepository.saveAll(dirtyQuotas.values());
            dirtyQuotas.clear();
        }
    }

    @Scheduled(cron = "0 0 0 1 * ?") // At midnight on the first day of every month
    public void resetMonthlyQuotas() {
        resetAllQuotas();
    }

    public synchronized void resetAllQuotas() {
        LOGGER.info("Resetting all quotas...");
        LocalDate now = LocalDate.now();
        Iterable<UserQuota> allQuotas = userQuotaRepository.findAll();
        for (UserQuota quota : allQuotas) {
            if (!now.isEqual(quota.getLastResetDate())) { // Check if it has already been reset this month
                quota.setQuotaUsed(0);
                quota.setLastResetDate(now); // Update the last reset date
                quotaCache.put(quota.getUserId(), quota);
                dirtyQuotas.put(quota.getUserId(), quota); // Mark as dirty
            }
        }
        flushQuotas();
    }

    @PreDestroy
    public void onShutdown() {
        LOGGER.info("Shutting down, flushing quotas...");
        flushQuotas();
    }

    @PostConstruct
    public void init() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::onShutdown));
        updateQuotasWithDefaultLastResetDate(); // Update quotas with default last reset date if necessary
        checkAndResetQuotasIfNecessary(); // Check and reset quotas if necessary
    }

    public void updateQuotasWithDefaultLastResetDate() {
        Iterable<UserQuota> allQuotas = userQuotaRepository.findAll();
        LocalDate defaultDate = LocalDate.now();
        for (UserQuota quota : allQuotas) {
            if (quota.getLastResetDate() == null) {
                quota.setLastResetDate(defaultDate);
                userQuotaRepository.save(quota);
            }
        }
    }

    public void checkAndResetQuotasIfNecessary() {
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfMonth = now.withDayOfMonth(1);
        boolean needReset = userQuotaRepository.findAll().stream()
                .anyMatch(quota -> quota.getLastResetDate() == null || !firstDayOfMonth.isEqual(quota.getLastResetDate()));
        if (needReset) {
            resetAllQuotas();
        }
    }
}
