package com.example.spring.aspect;

import com.example.spring.keycloakClient.UserResource;
import com.example.spring.model.UserQuota;
import com.example.spring.service.UserQuotaService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static com.example.spring.utils.HeadersUtil.parseUserFromHeader;

@Aspect
@Component
public class QuotaAspect {

    @Autowired
    UserQuotaService userQuotaService;

    @Autowired
    UserResource userResource;

    @Pointcut("execution(* com.example.spring.controller.CompanyController.getRandomCompanies(..))")
    public void getRandomCompaniesMethod() {}

    @Around("getRandomCompaniesMethod()")
    public Object checkQuota(ProceedingJoinPoint joinPoint) throws Throwable {
        String userId = parseUserFromHeader();

        UserQuota userQuota = userQuotaService.getQuotaForUser(userId);

        if (userQuota.getQuotaUsed() < userQuota.getQuotaAllocated()) {
            userQuotaService.updateQuotaForUser(userId, userQuota.getQuotaUsed() + 1);
            return joinPoint.proceed();
        } else {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Quota exceeded for user");
        }
    }
}
