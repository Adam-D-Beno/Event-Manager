package org.das.event_manager.service;

import org.das.event_manager.controller.UserController;
import org.das.event_manager.domain.User;
import org.das.event_manager.dto.UserDto;
import org.das.event_manager.entity.UserEntity;
import org.das.event_manager.mappers.UserEntityMapper;
import org.das.event_manager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;

    public UserService(UserRepository userRepository, UserEntityMapper userEntityMapper) {
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
    }

    public User register(User userToRegister) {
        LOGGER.info("Execute method register user: login = {} in UserService class", userToRegister.login());
        UserEntity userEntityToSave = userEntityMapper.toEntity(userToRegister);
        userEntityToSave.setRole("USER");
        UserEntity userSaved = userRepository.save(userEntityToSave);
        return userEntityMapper.toDomain(userSaved);
    }
}
