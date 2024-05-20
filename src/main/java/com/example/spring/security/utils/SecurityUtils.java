package com.example.spring.security.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SecurityUtils {

    public static String parseEmailFromHeader() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert sra != null;
        HttpServletRequest request = sra.getRequest();
        System.out.println(request.getHeader("X-Auth-Request-Email"));
        return request.getHeader("X-Auth-Request-Email");
    }

    public static String parseUserFromHeader() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert sra != null;
        HttpServletRequest request = sra.getRequest();
        return request.getHeader("X-Auth-Request-User");
    }
}
