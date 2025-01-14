package org.das.event_manager.mappers;

import org.das.event_manager.domain.Location;
import org.das.event_manager.domain.User;
import org.das.event_manager.entity.LocationEntity;
import org.das.event_manager.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {

    public UserEntity toEntity(User user) {
        return null;
    }

    public User toDomain(UserEntity userEntity) {
        return null;
    }
}
