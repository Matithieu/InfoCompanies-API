package com.example.spring.aspect;

import com.example.spring.exception.QuotaExceededException;
import com.example.spring.security.jwt.JwtUtilities;
import com.example.spring.service.user.UserService;
import com.example.spring.service.userQuota.UserQuotaService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class QuotaAspect {

    @Autowired
    private UserQuotaService userQuotaService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtilities jwtUtilities;

    @Pointcut("execution(* com.example.spring.service.company.CompanyService.searchCompanies(..))")
    public void searchCompaniesMethod() {}

    @Around("searchCompaniesMethod()")
    public Object checkQuota(ProceedingJoinPoint joinPoint) throws Throwable {
        // Récupérer l'ID de l'utilisateur
        String email = jwtUtilities.getEmailOfRequester();
        Long userId = userService.getUserByEmail(email).getId();
        // Vérifier le quota de recherche
        if (userQuotaService.hasRemainingSearches(userId)) {
            // Décrémenter le quota et permettre la recherche
            userQuotaService.decrementRemainingSearches(userId);
            return joinPoint.proceed();
        } else {
            // Refuser la recherche en raison d'un quota dépassé
            throw new QuotaExceededException("Quota de recherche dépassé pour cet utilisateur.");
        }
    }
}
