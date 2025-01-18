package org.das.event_manager.mappers;

import jakarta.validation.constraints.NotNull;
import org.das.event_manager.domain.User;
import org.das.event_manager.dto.SignUpRequest;
import org.das.event_manager.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.management.relation.Role;

@Validated
@Component
public class UserDtoMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDtoMapper.class);


    public UserDto toDto(@NotNull User user) {
        LOGGER.info("Execute method toDto in UserDtoMapper, user login: {}", user.login());
        return new UserDto(
                user.id(),
                user.login(),
                user.age(),
                user.role()
        );
    }

    public User toDomain(SignUpRequest signUpRequest) {
        return new User(
                null,
                signUpRequest.login(),
                signUpRequest.password(),
                signUpRequest.age(),
                ""
        );
    }
}
