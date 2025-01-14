package org.das.event_manager.service;

import org.das.event_manager.domain.User;
import org.das.event_manager.dto.UserDto;
import org.das.event_manager.entity.UserEntity;
import org.das.event_manager.mappers.UserEntityMapper;
import org.das.event_manager.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;

    public UserService(UserRepository userRepository, UserEntityMapper userEntityMapper) {
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
    }

    public User register(User user) {
        UserEntity userSaved = userRepository.save(userEntityMapper.toEntity(user));
        return userEntityMapper.toDomain(userSaved);
    }
}
