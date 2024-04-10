package com.example.spring.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class JwtUtils {

    public static Jwt getJwtFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
            return jwtAuthenticationToken.getToken();
        }
        return null;
    }

    public static String getClaimFromJwt(String claimName) {
        Jwt jwt = getJwtFromAuthentication();
        if (jwt != null) {
            return jwt.getClaim(claimName);
        }
        return null;
    }
}
