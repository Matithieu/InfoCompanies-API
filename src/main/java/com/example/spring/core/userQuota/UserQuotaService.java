package com.example.spring.core.userQuota;

import com.example.spring.core.appSettings.AppSettings;
import com.example.spring.core.appSettings.AppSettingsRepository;
import com.example.spring.common.utils.LogUtil;
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

@EnableScheduling
@Service
public class UserQuotaService {

    @Autowired
    AppSettingsRepository configRepository;

    @Autowired
    UserQuotaRepository userQuotaRepository;

    private final Cache<String, UserQuotaModel> quotaCache;
    private final Map<String, UserQuotaModel> dirtyQuotas;
    private final Lock readLock;
    private final Lock writeLock;

    @Autowired
    public UserQuotaService(AppSettingsRepository configRepository, UserQuotaRepository userQuotaRepository) {
        this.configRepository = configRepository;
        this.userQuotaRepository = userQuotaRepository;
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

    public UserQuotaModel getQuotaForUser(String userId) {
        readLock.lock();
        try {
            return quotaCache.get(userId, this::loadQuotaFromDatabase);
        } finally {
            readLock.unlock();
        }
    }

    public void createQuotaForUser(String userId, Integer quotaAllocated) {
        UserQuotaModel userQuota = new UserQuotaModel(userId, quotaAllocated, 0);
        userQuotaRepository.save(userQuota);
        quotaCache.put(userId, userQuota);
    }

    private UserQuotaModel loadQuotaFromDatabase(String userId) {
        return userQuotaRepository.findByUserId(userId).orElse(null);
    }

    public void updateQuotaForUser(String userId, Integer quotaUsed) {
        writeLock.lock();
        try {
            UserQuotaModel userQuota = getQuotaForUser(userId);
            userQuota.setQuotaUsed(quotaUsed);
            dirtyQuotas.put(userId, userQuota);
            quotaCache.put(userId, userQuota);
        } finally {
            writeLock.unlock();
        }
    }

    @Scheduled(fixedRate = 1000 * 60 * 3) // Every 3 minutes
    public void flushQuotas() {
        double loggingScale = 0.2;
        writeLock.lock();
        try {
            if (!dirtyQuotas.isEmpty()) {
                if (LogUtil.shouldLog(loggingScale)) { // Log at 20% scale
                    LogUtil.info("Flushing quotas...", Map.of());
                }
                userQuotaRepository.saveAll(dirtyQuotas.values());
                dirtyQuotas.clear();
            }
        } catch (Exception e) {
            LogUtil.error("Failed to flush quotas:", e);
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
        Iterable<UserQuotaModel> allQuotas = userQuotaRepository.findAll();
        LogUtil.info("Resetting all quotas. Last reset date: ", Map.of(
                "last_reset_date", lastResetDate
        ));

        writeLock.lock();
        try {
            // Check if it has already been this day
            if (now.isAfter(lastResetDate)) {
                for (UserQuotaModel quota : allQuotas) {
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
        LogUtil.info("Shutting down, flushing quotas.", Map.of());
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
        AppSettings config = configRepository.findTopByOrderByIdDesc().orElse(null);
        if (config == null) {
            config = new AppSettings();
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
        AppSettings config = configRepository.findTopByOrderByIdDesc().get();
        config.setLastResetQuotaDate(date);
        configRepository.save(config);
    }
}
