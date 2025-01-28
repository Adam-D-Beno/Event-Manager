package org.das.event_manager.service;

import org.das.event_manager.domain.User;
import org.das.event_manager.dto.SignInRequest;
import org.das.event_manager.security.CustomUserDetailService;
import org.das.event_manager.security.jwt.JwtTokenManager;
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
    private final JwtTokenManager jwtTokenManager;
    private final UserService userService;


    public AuthenticationService(
            AuthenticationManager authenticationManager,
            JwtTokenManager jwtTokenManager,
            UserService userService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenManager = jwtTokenManager;
        this.userService = userService;
    }

    public String authenticateUser(SignInRequest signInRequest) {
        LOGGER.info("Execute method authenticateUser user: login = {} in AuthenticationService class",
                signInRequest.login());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequest.login(),
                signInRequest.password()
        ));
        User foundUser = userService.findByLogin(signInRequest.login());
        return jwtTokenManager.generateJwtToken(foundUser);
    }


}
