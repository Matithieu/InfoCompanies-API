package com.example.spring.aspect;

import com.example.spring.DTO.User;
import com.example.spring.exception.QuotaExceededException;
import com.example.spring.keycloakClient.UserResource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.example.spring.security.JwtUtils.getClaimFromJwt;

@Aspect
@Component
public class QuotaAspect {

    @Autowired
    UserResource userResource;

    //@Pointcut("execution(* com.example.spring.service.company.CompanyService.searchCompanies(..))")
    @Pointcut("execution(* com.example.spring.controller.CompanyController.test())")
    public void searchCompaniesMethod() {}

    @Around("searchCompaniesMethod()")
    public Object checkQuota(ProceedingJoinPoint joinPoint) throws Throwable {
        String email = getClaimFromJwt("email");
        User user = userResource.getUserByEmail(email);
        int quota = Integer.parseInt(user.getQuota());
        if (quota > 0) {
            user.setQuota(String.valueOf(quota - 1));
            try {
                userResource.updateUser(user);
            } catch (Exception e) {
                throw new RuntimeException("Failed to update user quota");
            }
            return joinPoint.proceed();
        } else {
            throw new QuotaExceededException("Quota exceeded for user");
        }
    }
}
