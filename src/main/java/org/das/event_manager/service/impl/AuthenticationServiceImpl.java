package org.das.event_manager.service.impl;

import org.das.event_manager.domain.User;
import org.das.event_manager.dto.SignInRequest;
import org.das.event_manager.security.jwt.JwtTokenManager;
import org.das.event_manager.service.AuthenticationService;
import org.das.event_manager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private final AuthenticationManager authenticationManager;
    private final JwtTokenManager jwtTokenManager;
    private final UserService userService;


    public AuthenticationServiceImpl(
            AuthenticationManager authenticationManager,
            JwtTokenManager jwtTokenManager,
            UserService userService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenManager = jwtTokenManager;
        this.userService = userService;
    }

    @Override
    public String authenticateUser(SignInRequest signInRequest) {
        LOGGER.info("Execute method authenticateUser user: login = {} in AuthenticationServiceImpl class",
                signInRequest.login());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequest.login(),
                signInRequest.password()
        ));
        User foundUser = userService.findByLogin(signInRequest.login());
        return jwtTokenManager.generateJwtToken(foundUser);
    }

    @Override
    public User getCurrentAuthenticatedUser() {
        LOGGER.info("Execute method getCurrentAuthenticatedUserOrThrow");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            LOGGER.error("Authenticated user not found");

            throw  new IllegalStateException("Authenticated user not exist in context security");
        }
        return (User) authentication.getPrincipal();
    }
}
