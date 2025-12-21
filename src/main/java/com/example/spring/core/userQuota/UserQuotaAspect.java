package com.example.spring.core.userQuota;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static com.example.spring.utils.HeadersUtil.parseUserIdFromHeader;

@Aspect
@Component
public class UserQuotaAspect {

    @Autowired
    UserQuotaService userQuotaService;

    // Per-user lock management
    private final ConcurrentHashMap<String, ReentrantLock> userLocks = new ConcurrentHashMap<>();
    private final ScheduledExecutorService lockCleanupExecutor = Executors.newSingleThreadScheduledExecutor();

    // Pointcut that matches all methods within CompanyController
    @Pointcut("within(com.example.spring.app.company.CompanyController)")
    public void allMethodsInCompanyController() {}

    // Pointcut that matches the excluded methods
    @Pointcut("execution(* com.example.spring.app.company.CompanyController.searchCompaniesByName(..)) || " +
            "execution(* com.example.spring.app.company.CompanyController.scrapCompany(..)) || " +
            "execution(* com.example.spring.app.company.CompanyController.getCompaniesOnLandingByFilters(..))")
    public void excludedMethods() {}

    // Combined pointcut that includes all methods except the excluded ones
    @Pointcut("allMethodsInCompanyController() && !excludedMethods()")
    public void allMethodsExceptExcluded() {}

    @PostConstruct
    public void initCleanup() {
        // Schedule lock cleanup every 10 minutes
        lockCleanupExecutor.scheduleAtFixedRate(
                this::cleanupUnusedLocks,
                10, 10, TimeUnit.MINUTES
        );
    }

    @PreDestroy
    public void shutdown() {
        lockCleanupExecutor.shutdown();
    }

    private void cleanupUnusedLocks() {
        // Remove locks that are not currently held and have no waiting threads
        userLocks.entrySet().removeIf(entry -> {
            ReentrantLock lock = entry.getValue();
            return !lock.isLocked() && !lock.hasQueuedThreads();
        });
    }

    @Around("allMethodsExceptExcluded()")
    public Object checkQuota(ProceedingJoinPoint joinPoint) throws Throwable {
        String userId = parseUserIdFromHeader();

        // Get or create lock for this specific user
        ReentrantLock userLock = userLocks.computeIfAbsent(userId, k -> new ReentrantLock());

        userLock.lock();
        try {
            UserQuotaModel userQuota = userQuotaService.getQuotaForUser(userId);

            if (userQuota.getQuotaUsed() < userQuota.getQuotaAllocated()) {
                userQuotaService.updateQuotaForUser(userId, userQuota.getQuotaUsed() + 1);
                return joinPoint.proceed();
            }

            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Quota exceeded for user");
        } finally {
            userLock.unlock();
        }
    }
}
