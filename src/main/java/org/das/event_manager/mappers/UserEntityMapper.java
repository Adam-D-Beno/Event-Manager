package org.das.event_manager.mappers;

import jakarta.validation.constraints.NotNull;
import org.das.event_manager.domain.User;
import org.das.event_manager.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Validated
@Component
public class UserEntityMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserEntityMapper.class);

    public UserEntity toEntity(@NotNull User user) {
        LOGGER.info("Execute method toEntity in UserEntityMapper, user login: {}", user.login());
        return new UserEntity(
                null,
                user.login(),
                user.password(),
                user.age(),
                user.role()
        );
    }

    public User toDomain(@NotNull UserEntity userEntity) {
        LOGGER.info("Execute method toDomain in UserEntityMapper, user login: {}", userEntity.getLogin());
        return new User(
                userEntity.getId(),
                userEntity.getLogin(),
                "",
                userEntity.getAge(),
                userEntity.getRole()
        );
    }
}
