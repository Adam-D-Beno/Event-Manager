package org.das.event_manager.listeners;

import org.das.event_manager.entity.UserEntity;
import org.das.event_manager.repository.UserRepository;
import org.das.event_manager.utils.Role;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserListener {
    public static final String defaultAdmin = "admin";
    public static final String defaultUser = "user";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DefaultUserListener(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener
    public void initialiseDefaultUsers(ApplicationStartedEvent applicationStartedEvent) {
        if (!userRepository.existsByLogin(defaultAdmin)) {
            userRepository.save(createDefaultUser(defaultAdmin, Role.ADMIN));
        }
        if (!userRepository.existsByLogin(defaultUser)) {
            userRepository.save(createDefaultUser(defaultUser, Role.USER));
        }
    }

    private UserEntity createDefaultUser(String login, Role role) {
        return new UserEntity(null, login, passwordEncoder.encode(login), 1988, role);
    }
}
