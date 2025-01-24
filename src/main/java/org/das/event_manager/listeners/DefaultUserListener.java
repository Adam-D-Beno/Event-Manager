package org.das.event_manager.listeners;

import org.das.event_manager.entity.UserEntity;
import org.das.event_manager.repository.UserRepository;
import org.das.event_manager.utils.Role;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserListener {
    public static final String defaultAdmin = "admin";
    public static final String defaultUser = "user";
    private final UserRepository userRepository;

    public DefaultUserListener(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        return new UserEntity(null, login, login, 1988, role);
    }
}
