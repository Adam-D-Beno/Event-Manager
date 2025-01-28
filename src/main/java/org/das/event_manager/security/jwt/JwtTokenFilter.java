package org.das.event_manager.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.das.event_manager.domain.User;
import org.das.event_manager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final JwtTokenManager jwtTokenManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);

    public JwtTokenFilter(
            @Lazy UserService userService,
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
        LOGGER.info("Execute JwtTokenFilter");
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = authorizationHeader.substring(7);
        String userLoginFromToken;
        String userRoleFromToken;
        try {
            userLoginFromToken = jwtTokenManager.getLoginFromToken(jwt);
//            userRoleFromToken = jwtTokenManager.getRoleFromToken(jwt);
        } catch (Exception e) {
            LOGGER.error("Error while reading jwt", e);
            filterChain.doFilter(request,response);
            return;
        }
        //todo get role from jwt token
        // what keep in UsernamePasswordAuthenticationToken on parameter Object principal
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userLoginFromToken,
                null,
                List.of(new SimpleGrantedAuthority("user"))
        );
        addSecurityContextHolder(authenticationToken);
        filterChain.doFilter(request, response);
    }

    private void addSecurityContextHolder(UsernamePasswordAuthenticationToken token) {
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}
