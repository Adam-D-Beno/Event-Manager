package org.das.event_manager.dto.mappers.impl;

import jakarta.validation.constraints.NotNull;
import org.das.event_manager.domain.User;
import org.das.event_manager.domain.entity.UserEntity;
import org.das.event_manager.dto.SignUpRequest;
import org.das.event_manager.dto.UserResponseDto;
import org.das.event_manager.dto.mappers.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
public class UserMapperImpl implements UserMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserMapperImpl.class);

    @Override
    public UserResponseDto toDto(User user) {
        LOGGER.info("Execute method toDto in UserDtoMapper, user login: {}", user.login());
        return new UserResponseDto(
                user.id(),
                user.login(),
                user.age(),
                user.userRole()
        );
    }

    @Override
    public User toDomain(SignUpRequest signUpRequest) {
        LOGGER.info("Execute method toDomain in UserDtoMapper, user login: {}", signUpRequest.login());
        return new User(
                null,
                signUpRequest.login(),
                signUpRequest.password(),
                signUpRequest.age(),
                null
        );
    }

    @Override
    public UserEntity toEntity(User user) {
        LOGGER.info("Execute method toEntity in UserEntityMapper, user login: {}", user.login());
        return new UserEntity(
                null,
                user.login(),
                user.passwordHash(),
                user.age(),
                user.userRole()
        );
    }

    @Override
    public User toDomain(UserEntity userEntity) {
        LOGGER.info("Execute method toDomain in UserEntityMapper, user login: {}", userEntity.getLogin());
        return new User(
                userEntity.getId(),
                userEntity.getLogin(),
                "",
                userEntity.getAge(),
                userEntity.getUserRole()
        );
    }
}
