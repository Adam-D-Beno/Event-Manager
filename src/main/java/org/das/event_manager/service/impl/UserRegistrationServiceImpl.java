package org.das.event_manager.service.impl;

import lombok.RequiredArgsConstructor;
import org.das.event_manager.domain.User;
import org.das.event_manager.domain.UserRole;
import org.das.event_manager.service.UserRegistrationService;
import org.das.event_manager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationServiceImpl.class);
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User register(User signUpRequest) {
        LOGGER.info("Execute method register user: login = {} in UserRegistrationService class"
                ,signUpRequest.login());
        if (userService.isUserExistsByLogin(signUpRequest.login())) {
            LOGGER.error("User with login={} already exist", signUpRequest.login());
            throw new IllegalArgumentException("User with such login = %s already exist"
                    .formatted(signUpRequest.login()));
        }
        var userToSave = new User(
                null,
                signUpRequest.login(),
                passwordEncoder.encode(signUpRequest.passwordHash()),
                signUpRequest.age(),
                UserRole.USER
        );
        return userService.save(userToSave);
    }
}
