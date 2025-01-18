package org.das.event_manager.service;

import org.das.event_manager.dto.SignInRequest;
import org.das.event_manager.security.CustomUserDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);
    private final AuthenticationManager authenticationManager;


    public AuthenticationService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void authenticateUser(SignInRequest signInRequest) {
        LOGGER.info("Execute method authenticateUser user: login = {} in AuthenticationService class",
                signInRequest.login());

        Authentication authenticated = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequest.login(),
                signInRequest.password()
        ));
        addSecurityContextHolder(authenticated);
    }

    private void addSecurityContextHolder(Authentication authenticated) {
        SecurityContextHolder.getContext().setAuthentication(authenticated);
    }
}
