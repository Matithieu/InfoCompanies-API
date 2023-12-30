package com.example.spring.security;

import com.example.spring.model.UserSession;
import com.example.spring.service.UserSessionService;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.*;

public class SessionFilter implements Filter {

    private final UserSessionService userSessionService;
    private static final Set<String> ALLOWED_PATHS = new HashSet<>(Arrays.asList("/auth/login", "/auth/register"));

    public SessionFilter(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

    @Override
    public void doFilter(
            ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

        if (ALLOWED_PATHS.contains(path)) {
            chain.doFilter(request, response);
            return;
        }

        Cookie[] allCookies = req.getCookies();
        boolean validSession = false;

        if (allCookies != null) {
            Cookie sessionCookie =
                    Arrays.stream(allCookies).filter(x -> x.getName().equals("session_id"))
                            .findFirst().orElse(null);

            if (sessionCookie != null) {
                sessionCookie.setHttpOnly(true);
                sessionCookie.setSecure(true);
                res.addCookie(sessionCookie);

                // check if session is valid
                String sessionId = sessionCookie.getValue();
                validSession = true;
            }
        }

        if (validSession) {
            chain.doFilter(req, res);
        } else {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Session");
        }
    }
}