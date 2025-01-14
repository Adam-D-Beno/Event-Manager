package org.das.event_manager.mappers;

import org.das.event_manager.domain.User;
import org.das.event_manager.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper {
    public UserDto toDto(User user) {
        return null;
    }

    public User toDomain(UserDto userDto) {
        return null;
    }
}
