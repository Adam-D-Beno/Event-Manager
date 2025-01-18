package org.das.event_manager.mappers;

import org.das.event_manager.domain.Location;
import org.das.event_manager.domain.User;
import org.das.event_manager.entity.LocationEntity;
import org.das.event_manager.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserEntityMapper.class);

    public UserEntity toEntity(User user) {
        return new UserEntity(
                null,
                user.login(),
                user.password(),
                user.age(),
                user.role()
        );
    }

    public User toDomain(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getLogin(),
                "",
                userEntity.getAge(),
                userEntity.getRole()
        );
    }
}
