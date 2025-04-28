package org.das.event_manager.service.impl;

import org.das.event_manager.domain.User;
import org.das.event_manager.domain.entity.UserEntity;
import org.das.event_manager.dto.mappers.UserMapper;
import org.das.event_manager.repository.UserRepository;
import org.das.event_manager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User save(User userToSave) {
        LOGGER.info("Execute method save user: login = {} in UserServiceImpl class", userToSave.login());
        UserEntity entityForSave = userMapper.toEntity(userToSave);
        UserEntity saved = userRepository.save(entityForSave);
        return userMapper.toDomain(saved);
    }

    @Override
    public User findByLogin(String login) {
        LOGGER.info("Execute method getUserByLogin user: login = {} in UserServiceImpl class", login);
        return userRepository.findByLogin(login)
                .map(userMapper::toDomain)
                .orElseThrow(() -> {
                    LOGGER.error("User with login={} not found", login);
                    return new UsernameNotFoundException("User not found");
                });
    }

    @Override
    public User findById(Long userId) {
        LOGGER.info("Execute method getUserById user: id = {} in UserServiceImpl class", userId);
        return userRepository.findById(userId)
                .map(userMapper::toDomain)
                .orElseThrow(() -> {
                   LOGGER.error("User with id={} not found", userId);
                   return new UsernameNotFoundException("User not found");
                });
    }

    @Override
    public boolean isUserExistsByLogin(String login) {
        LOGGER.info("Execute method userExistByLogin user: login = {} in UserServiceImpl class", login);
        return userRepository.existsByLogin(login);
    }
}
