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

import static com.example.spring.security.utils.SecurityUtils.parseEmailFromHeader;
import static com.example.spring.security.utils.SecurityUtils.parseUserFromHeader;

@Aspect
@Component
public class QuotaAspect {

    @Autowired
    UserResource userResource;

    @Pointcut("execution(* com.example.spring.controller.CompanyController.getRandomCompanies())")
    public void getRandomCompaniesMethod() {}

    @Around("getRandomCompaniesMethod()")
    public Object checkQuota(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("TEST " + parseUserFromHeader());
        String email = parseEmailFromHeader();
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
