package org.das.event_manager.service;

import org.das.event_manager.domain.User;
import org.das.event_manager.domain.entity.UserEntity;
import org.das.event_manager.dto.mappers.UserMapper;
import org.das.event_manager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public User save(User userToSave) {
        LOGGER.info("Execute method save user: login = {} in UserService class", userToSave.login());
        UserEntity entityForSave = userMapper.toEntity(userToSave);
        UserEntity saved = userRepository.save(entityForSave);
        return userMapper.toDomain(saved);
    }

    public User findByLogin(String login) {
        LOGGER.info("Execute method getUserByLogin user: login = {} in UserService class", login);
        return userRepository.findByLogin(login)
                .map(userMapper::toDomain)
                .orElseThrow(() -> {
                    LOGGER.error("User with login={} not found", login);
                    return new UsernameNotFoundException("User not found");
                });
    }

    public User findById(Long userId) {
        LOGGER.info("Execute method getUserById user: id = {} in UserService class", userId);
        return userRepository.findById(userId)
                .map(userMapper::toDomain)
                .orElseThrow(() -> {
                   LOGGER.error("User with id={} not found", userId);
                   return new UsernameNotFoundException("User not found");
                });
    }

    public boolean isUserExistsByLogin(String login) {
        LOGGER.info("Execute method userExistByLogin user: login = {} in UserService class", login);
        return userRepository.existsByLogin(login);
    }
}
