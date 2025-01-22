package org.das.event_manager.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.das.event_manager.security.CustomUserDetailService;
import org.das.event_manager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final JwtTokenManager jwtTokenManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtFilter.class);

    public JwtFilter(
            UserService userService,
            JwtTokenManager jwtTokenManager
    ) {
        this.userService = userService;
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        LOGGER.info("Execute JwtFilter");
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = authorizationHeader.substring(7);
        String userLogin = jwtTokenManager.getLoginFromToken(jwt);


    }
}
