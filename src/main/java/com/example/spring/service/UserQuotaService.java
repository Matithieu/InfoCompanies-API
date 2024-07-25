package com.example.spring.service;

import com.example.spring.model.Config;
import com.example.spring.model.UserQuota;
import com.example.spring.repository.ConfigRepository;
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
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

@EnableScheduling
@Service
public class UserQuotaService {

    private static final Logger LOGGER = Logger.getLogger(UserQuotaService.class.getName());

    @Autowired
    ConfigRepository configRepository;

    @Autowired
    UserQuotaRepository userQuotaRepository;

    private final Cache<String, UserQuota> quotaCache;
    private final Map<String, UserQuota> dirtyQuotas;
    private final Lock readLock;
    private final Lock writeLock;

    @Autowired
    public UserQuotaService() {
        long cacheExpireAfterWrite = 30;
        long cacheMaximumSize = 1000;
        this.quotaCache = Caffeine.newBuilder()
                .expireAfterWrite(cacheExpireAfterWrite, TimeUnit.MINUTES)
                .maximumSize(cacheMaximumSize)
                .build();
        this.dirtyQuotas = new ConcurrentHashMap<>();
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
    }

    public UserQuota getQuotaForUser(String userId) {
        readLock.lock();
        try {
            return quotaCache.get(userId, this::loadQuotaFromDatabase);
        } finally {
            readLock.unlock();
        }
    }

    public void createQuotaForUser(String userId, int quotaAllocated) {
        UserQuota userQuota = new UserQuota(userId, quotaAllocated, 0);
        userQuotaRepository.save(userQuota);
        quotaCache.put(userId, userQuota);
    }

    private UserQuota loadQuotaFromDatabase(String userId) {
        return userQuotaRepository.findByUserId(userId).orElse(null);
    }

    public void updateQuotaForUser(String userId, int quotaUsed) {
        writeLock.lock();
        try {
            UserQuota userQuota = getQuotaForUser(userId);
            userQuota.setQuotaUsed(quotaUsed);
            dirtyQuotas.put(userId, userQuota);
            quotaCache.put(userId, userQuota);
        } finally {
            writeLock.unlock();
        }
    }

    @Scheduled(fixedRate = 1000 * 60 * 3) // Every 3 minutes
    public void flushQuotas() {
        LOGGER.info("Scheduled task running...");
        writeLock.lock();
        try {
            if (!dirtyQuotas.isEmpty()) {
                LOGGER.info("Flushing quotas...");
                userQuotaRepository.saveAll(dirtyQuotas.values());
                dirtyQuotas.clear();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to flush quotas", e);
        } finally {
            writeLock.unlock();
        }
    }

    @Scheduled(cron = "0 0 0 * * *") // Reset every day at midnight
    public void resetMonthlyQuotas() {
        resetAllQuotas();
    }

    public void resetAllQuotas() {
        LocalDate now = LocalDate.now(ZoneId.systemDefault());
        LocalDate lastResetDate = configRepository.findTopByOrderByIdDesc().get().getLastResetQuotaDate();
        Iterable<UserQuota> allQuotas = userQuotaRepository.findAll();
        LOGGER.info("Resetting all quotas. Last reset date: " + lastResetDate);
        writeLock.lock();
        try {
            // Check if it has already been this day
            if (now.isAfter(lastResetDate)) {
                for (UserQuota quota : allQuotas) {
                    quota.setQuotaUsed(0);
                    quotaCache.put(quota.getUserId(), quota);
                    dirtyQuotas.put(quota.getUserId(), quota);
                }
                updateLastResetQuotaDate(now);
            }
            flushQuotas();
        } finally {
            writeLock.unlock();
        }
    }

    @PreDestroy
    public void onShutdown() {
        LOGGER.info("Shutting down, flushing quotas...");
        flushQuotas();
    }

    @PostConstruct
    public void init() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::onShutdown));
        updateQuotasWithDefaultLastResetDate();
        checkAndResetQuotasIfNecessary();
    }

    public void updateQuotasWithDefaultLastResetDate() {
        LocalDate defaultDate = LocalDate.now(ZoneId.systemDefault());
        Config config = configRepository.findTopByOrderByIdDesc().orElse(null);
        if (config == null) {
            config = new Config();
            config.setLastResetQuotaDate(defaultDate);
            configRepository.save(config);
        }
    }

    public void checkAndResetQuotasIfNecessary() {
        LocalDate now = LocalDate.now(ZoneId.systemDefault());
        LocalDate lastResetDate = configRepository.findTopByOrderByIdDesc().get().getLastResetQuotaDate();
        if (now.isAfter(lastResetDate)) {
            resetAllQuotas();
        }
    }

    private void updateLastResetQuotaDate(LocalDate date) {
        Config config = configRepository.findTopByOrderByIdDesc().get();
        config.setLastResetQuotaDate(date);
        configRepository.save(config);
    }
}
