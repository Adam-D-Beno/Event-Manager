package org.das.event_manager.service;

import org.das.event_manager.domain.User;
import org.das.event_manager.domain.entity.UserEntity;
import org.das.event_manager.mappers.UserEntityMapper;
import org.das.event_manager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;
    private final UserRegistrationService userRegistrationService;

    public UserService(
            UserRepository userRepository,
            UserEntityMapper userEntityMapper,
            UserRegistrationService userRegistrationService
    ) {
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
        this.userRegistrationService = userRegistrationService;
    }


    public User save(User signUpRequest) {
        LOGGER.info("Execute method register user: login = {} in UserService class", signUpRequest.login());
        User registered = userRegistrationService.register(signUpRequest);
        UserEntity userToSave = userEntityMapper.toEntity(registered);
        UserEntity saved = userRepository.save(userToSave);
        return userEntityMapper.toDomain(saved);
    }

    public void userExistByLogin(String login) {
        if (userRepository.existsByLogin(login)) {
            throw new IllegalArgumentException("User already taken");
        }
    }

    public User findByLogin(String login) {
        LOGGER.info("Execute method getUserByLogin user: login = {} in UserService class", login);
        return userRepository.findByLogin(login)
                .map(userEntityMapper::toDomain)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
