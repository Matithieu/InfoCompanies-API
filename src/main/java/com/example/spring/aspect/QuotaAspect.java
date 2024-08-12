package com.example.spring.aspect;

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

    // Pointcut that matches all methods within CompanyController
    @Pointcut("within(com.example.spring.controller.CompanyController)")
    public void allMethodsInCompanyController() {}

    // Pointcut that matches the excluded methods
    @Pointcut("execution(* com.example.spring.controller.CompanyController.searchCompaniesByName(..)) || " +
            "execution(* com.example.spring.controller.CompanyController.scrapCompany(..)) || " +
            "execution(* com.example.spring.controller.CompanyController.getCompaniesByIds(..)) || " +
            "execution(* com.example.spring.controller.CompanyController.getCompaniesOnLandingByFilters(..))")
    public void excludedMethods() {}

    // Combined pointcut that includes all methods except the excluded ones
    @Pointcut("allMethodsInCompanyController() && !excludedMethods()")
    public void allMethodsExceptExcluded() {}

    @Around("allMethodsExceptExcluded()")
    public Object checkQuota(ProceedingJoinPoint joinPoint) throws Throwable {
        String userId = parseUserFromHeader();

        UserQuota userQuota = userQuotaService.getQuotaForUser(userId);

        if (userQuota.getQuotaUsed() < userQuota.getQuotaAllocated()) {
            synchronized (this) {
                if (userQuota.getQuotaUsed() < userQuota.getQuotaAllocated()) {
                    userQuotaService.updateQuotaForUser(userId, userQuota.getQuotaUsed() + 1);
                    return joinPoint.proceed();
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Quota exceeded for user");
    }
}
