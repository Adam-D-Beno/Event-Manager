package org.das.event_manager.service;

import org.das.event_manager.domain.User;
import org.das.event_manager.entity.UserEntity;
import org.das.event_manager.mappers.UserEntityMapper;
import org.das.event_manager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
                       UserEntityMapper userEntityMapper,
                       PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(User signUpRequest) {
        LOGGER.info("Execute method register user: login = {} in UserService class", signUpRequest.login());
        if (userRepository.existsByLogin(signUpRequest.login())) {
            throw new IllegalArgumentException("User already taken");
        }
        var hashedPass = passwordEncoder.encode(signUpRequest.password());
        UserEntity userToSave = userEntityMapper.toEntity(signUpRequest);
        userToSave.setPassword(hashedPass);
        userToSave.setRole("USER");
        UserEntity userSaved = userRepository.save(userToSave);
        return userEntityMapper.toDomain(userSaved);
    }
}
