package com.example.spring.security.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class SecurityUtils {

    // Find the corresponding header of nginx: proxy_set_header X-User $user;
    public static String parseEmailFromHeader() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert sra != null;
        HttpServletRequest request = sra.getRequest();
        return request.getHeader("X-Email");
    }

    public static String parseUserFromHeader() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert sra != null;
        HttpServletRequest request = sra.getRequest();
        return request.getHeader("X-User");
    }

    // Function to display all headers
    public static Map<String, String> getAllHeaders() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert sra != null;
        HttpServletRequest request = sra.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headers = new HashMap<>();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }

        return headers;
    }
}
