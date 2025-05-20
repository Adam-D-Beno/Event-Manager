package org.das.event_manager.listeners;

import org.das.event_manager.domain.entity.UserEntity;
import org.das.event_manager.repository.UserRepository;
import org.das.event_manager.domain.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultUserListener.class);
    public static final String DEFAULT_ADMIN = "admin";
    public static final String DEFAULT_USER = "user";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DefaultUserListener(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener
    public void initialiseDefaultUsers(ApplicationStartedEvent applicationStartedEvent) {
        LOGGER.info("Execute method initialiseDefaultUsers");

        if (!userRepository.existsByLogin(DEFAULT_ADMIN)) {
            userRepository.save(createDefaultUser(DEFAULT_ADMIN, UserRole.ADMIN));
        }
        if (!userRepository.existsByLogin(DEFAULT_USER)) {
            userRepository.save(createDefaultUser(DEFAULT_USER, UserRole.USER));
        }
    }

    private UserEntity createDefaultUser(String login, UserRole userRole) {
        LOGGER.info("Execute method createDefaultUser");

        return new UserEntity(null, login, passwordEncoder.encode(login), 1988, userRole);
    }
}
